package az.spring.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String code, String message) {
        super(message);
    }

}
