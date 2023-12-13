package az.spring.service;

import az.spring.entity.User;
import az.spring.entity.VerificationToken;
import az.spring.exception.UserAlreadyExistsException;
import az.spring.exception.error.ErrorMessage;
import az.spring.mapper.UserMapper;
import az.spring.repository.UserRepository;
import az.spring.request.UserRegistration;
import az.spring.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;
    private final JwtService jwtService;
    private final EmailService emailService;

    public UserResponse registerUser(UserRegistration userRegistration) throws UserAlreadyExistsException {
        if (userRepository.findByEmailIgnoreCase(userRegistration.getEmail()).isPresent() ||
                userRepository.findByUsernameIgnoreCase(userRegistration.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(HttpStatus.BAD_REQUEST.name(), ErrorMessage.ALREADY_EXISTS);
        }
        User user = userMapper.requestToModel(userRegistration);
        user.setPassword(encryptionService.encryptPassword(userRegistration.getPassword()));

        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        return userMapper.modelToResponse(userRepository.save(user));
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