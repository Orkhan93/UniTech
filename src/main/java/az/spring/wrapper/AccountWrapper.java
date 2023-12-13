package az.spring.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountWrapper {

    private Long id;
    private String accountNumber;
    private Boolean status;
    private Double balance;

}