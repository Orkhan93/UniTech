package az.spring.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequest {

    private String pin;
    private String password;

}