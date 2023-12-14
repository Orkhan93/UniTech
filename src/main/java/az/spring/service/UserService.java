package az.spring.service;

import az.spring.constant.UniTech;
import az.spring.entity.User;
import az.spring.entity.VerificationToken;
import az.spring.exception.*;
import az.spring.exception.error.ErrorMessage;
import az.spring.mapper.UserMapper;
import az.spring.repository.UserRepository;
import az.spring.repository.VerificationRepository;
import az.spring.request.ChangePasswordRequest;
import az.spring.request.ForgotPasswordRequest;
import az.spring.request.UserLoginRequest;
import az.spring.request.UserRegistration;
import az.spring.response.UserResponse;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final VerificationRepository verificationRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegistration userRegistration) throws UserAlreadyExistsException {
        log.info("Inside userRegistration {}", userRegistration);
        if (userRepository.findByEmailIgnoreCase(userRegistration.getEmail()).isPresent() ||
                userRepository.findByPinEqualsIgnoreCase(userRegistration.getPin()).isPresent()) {
            throw new UserAlreadyExistsException(BAD_REQUEST.name(), ErrorMessage.USER_ALREADY_EXISTS);
        }
        User user = userMapper.requestToModel(userRegistration);
        user.setPassword(encryptionService.encryptPassword(userRegistration.getPassword()));

        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        log.info("Inside register {}", user);
        return userMapper.modelToResponse(userRepository.save(user));
    }

    public String loginUser(UserLoginRequest userLoginRequest) throws UserNotVerifiedException, EmailFailureException {
        log.info("Inside userLoginRequest {}", userLoginRequest);
        Optional<User> optionalUser = userRepository.findByPinEqualsIgnoreCase(userLoginRequest.getPin());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encryptionService.verifyPassword(userLoginRequest.getPassword(), user.getPassword())) {
                if (user.getEmailVerified()) {
                    return jwtService.generateJwt(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.isEmpty() ||
                            verificationTokens.get(0).getCreatedTimestamp()
                                    .before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationRepository.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest, Long userId) {
        log.info("Inside changePassword {}", changePasswordRequest);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND));
        if (!encryptionService.verifyPassword(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException(BAD_REQUEST.name(), ErrorMessage.INCORRECT_PASSWORD);
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new IncorrectPasswordException(BAD_REQUEST.name(), ErrorMessage.NOT_MATCHES);
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        log.info("Inside savedUser {}", user);
        userRepository.save(user);
    }

    public ResponseEntity<String> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        log.info("Inside forgotPassword {}", forgotPasswordRequest);
        Optional<User> user = userRepository.findByEmailIgnoreCase(forgotPasswordRequest.getEmail());
        if (user.isPresent()) {
            emailService.forgetMail(user.get().getEmail(), UniTech.BY_UNITECH, user.get().getPassword());
            return ResponseEntity.status(OK).body(UniTech.CHECK_EMAIL);
        } else
            return ResponseEntity.status(BAD_REQUEST).body(ErrorMessage.USER_NOT_FOUND);
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> tokenOptional = verificationRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            VerificationToken verificationToken = tokenOptional.get();
            User user = verificationToken.getUser();
            if (!user.getEmailVerified()) {
                user.setEmailVerified(true);
                userRepository.save(user);
                verificationRepository.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    private VerificationToken createVerificationToken(User user) {
        log.info("Inside createVerificationToken {}", user);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJwt(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        log.info("Inside verificationToken {}", verificationToken);
        return verificationToken;
    }

}