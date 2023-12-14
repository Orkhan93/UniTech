package az.spring.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountWrapper {

    private Long id;
    private String accountNumber;
    private Boolean status;
    private BigDecimal balance;

}