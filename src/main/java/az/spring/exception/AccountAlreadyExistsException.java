package az.spring.exception;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String code, String message) {
        super(message);
    }

}
