package az.spring.response;

import az.spring.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountResponse {

    private String accountNumber;
    private Boolean status;
    private Double balance;

    @JsonIgnore
    private User user;

}