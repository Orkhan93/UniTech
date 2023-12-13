package az.spring.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {

    private String username;
    private String pin;
    private String email;
    private String password;

}