package az.spring.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TransferRequest {

    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;

}