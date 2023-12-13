package az.spring.request;

import az.spring.constraint.validation.Password;
import az.spring.constraint.validation.Pin;
import az.spring.constraint.validation.Username;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegistration {

    @Username
    private String username;

    @Pin
    private String pin;

    @Password
    private String password;

    @NotNull
    @NotBlank
    @Email
    private String email;

}