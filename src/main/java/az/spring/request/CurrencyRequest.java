package az.spring.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CurrencyRequest {

    private Long id;
    private String currencyType;
    private BigDecimal rate;

}