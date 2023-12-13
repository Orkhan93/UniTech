package az.spring.controller;

import az.spring.request.UserRegistration;
import az.spring.request.UserRequest;
import az.spring.response.UserResponse;
import az.spring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    private ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistration registration) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(registration));
    }

}