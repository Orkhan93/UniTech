package az.spring.request;

import az.spring.constraint.validation.Password;
import az.spring.constraint.validation.Pin;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequest {

    @Pin
    private String pin;

    @Password
    private String password;

}