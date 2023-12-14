package az.spring.request;

import az.spring.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountRequest {

    private String accountNumber;
    private Boolean status;
    private BigDecimal balance;
    private User user;

}