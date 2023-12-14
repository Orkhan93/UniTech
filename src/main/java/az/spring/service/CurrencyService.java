package az.spring.service;

import az.spring.entity.Currency;
import az.spring.exception.IllegalArgumentException;
import az.spring.exception.error.ErrorMessage;
import az.spring.repository.CurrencyRepository;
import az.spring.response.CurrencyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    @Value("${currency.api.url}")
    private String url;

    private final RestTemplate restTemplate;
    private final CurrencyRepository currencyRepository;

    public CurrencyResponse allCurrencyRates() {
        return restTemplate.getForObject(url, CurrencyResponse.class);
    }

    public double getSpecificExchangeRate(String sourceCurrency, String targetCurrency, LocalDate date) {
        CurrencyResponse response = restTemplate.getForObject(url, CurrencyResponse.class);
        if (response == null) {
            throw new IllegalArgumentException(HttpStatus.BAD_REQUEST.name(), ErrorMessage.INVALID_DATA);
        }
        double sourceRate = response.getRates().get(sourceCurrency);
        double targetRate = response.getRates().get(targetCurrency);
        if (sourceRate == 0 || targetRate == 0) {
            throw new IllegalArgumentException(HttpStatus.BAD_REQUEST.name(), ErrorMessage.INVALID_PAIR);
        }
        Currency currency = new Currency();
        currency.setCurrencyType(sourceCurrency);
        currency.setRate(targetRate / sourceRate);
        currency.setUpdatedDate(LocalDateTime.now());
        currencyRepository.save(currency);
        return targetRate / sourceRate;
    }

    public double convertAmount(double amount, String source, String target, LocalDate date) {
        double exchangeRate = getSpecificExchangeRate(source, target, date);
        if (exchangeRate == 0) {
            throw new IllegalArgumentException(HttpStatus.BAD_REQUEST.name(), ErrorMessage.INVALID_PAIR);
        }
        return amount * exchangeRate;
    }

}