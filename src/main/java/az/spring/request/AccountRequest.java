package az.spring.request;

import az.spring.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountRequest {

    private String accountNumber;
    private Boolean status;
    private Double balance;
    private User user;

}