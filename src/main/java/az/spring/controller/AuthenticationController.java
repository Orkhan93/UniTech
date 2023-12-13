package az.spring.controller;

import az.spring.constraint.validation.Password;
import az.spring.exception.EmailFailureException;
import az.spring.exception.UserNotVerifiedException;
import az.spring.request.ChangePasswordRequest;
import az.spring.request.UserLoginRequest;
import az.spring.request.UserRegistration;
import az.spring.response.UserJwtResponse;
import az.spring.response.UserLoginResponse;
import az.spring.response.UserResponse;
import az.spring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    private ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistration registration) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(registration));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginRequest userLogin) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(userLogin));
    }

    @PutMapping("/change-password/{userId}")
    public void changePassword(@RequestBody ChangePasswordRequest request, @PathVariable(name = "userId") Long userId) {
        userService.changePassword(request, userId);
    }

    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token) {
        if (userService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}