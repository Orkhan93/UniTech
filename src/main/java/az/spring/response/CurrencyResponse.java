package az.spring.response;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Setter
@Getter
public class CurrencyResponse {

    private Long id;
    private String currencyType;
    private Map<String, Double> rates;

}