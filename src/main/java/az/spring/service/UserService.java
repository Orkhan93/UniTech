package az.spring.service;

import az.spring.entity.User;
import az.spring.entity.VerificationToken;
import az.spring.exception.*;
import az.spring.exception.error.ErrorMessage;
import az.spring.mapper.UserMapper;
import az.spring.repository.UserRepository;
import az.spring.repository.VerificationRepository;
import az.spring.request.ChangePasswordRequest;
import az.spring.request.UserLoginRequest;
import az.spring.request.UserRegistration;
import az.spring.response.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
        if (userRepository.findByEmailIgnoreCase(userRegistration.getEmail()).isPresent() ||
                userRepository.findByPinEqualsIgnoreCase(userRegistration.getPin()).isPresent()) {
            throw new UserAlreadyExistsException(BAD_REQUEST.name(), ErrorMessage.ALREADY_EXISTS);
        }
        User user = userMapper.requestToModel(userRegistration);
        user.setPassword(encryptionService.encryptPassword(userRegistration.getPassword()));

        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        return userMapper.modelToResponse(userRepository.save(user));
    }

    public String loginUser(UserLoginRequest userLoginRequest) throws UserNotVerifiedException, EmailFailureException {
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
        User author = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND));
        if (!encryptionService.verifyPassword(changePasswordRequest.getOldPassword(), author.getPassword())) {
            throw new IncorrectPasswordException(BAD_REQUEST.name(), ErrorMessage.INCORRECT_PASSWORD);
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new IncorrectPasswordException(BAD_REQUEST.name(), ErrorMessage.NOT_MATCHES);
        }
        author.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(author);
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
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJwt(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

}