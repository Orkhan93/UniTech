package az.spring.exception;

public class EmailFailureException extends RuntimeException {

    public EmailFailureException(String code, String message) {
        super(message);
    }

}
