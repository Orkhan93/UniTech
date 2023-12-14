package az.spring.response;

import az.spring.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountResponse {

    private String accountNumber;
    private Boolean status;
    private BigDecimal balance;

    @JsonIgnore
    private User user;

}