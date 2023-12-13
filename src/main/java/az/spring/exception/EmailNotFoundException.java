package az.spring.exception;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String code, String message) {
        super(message);
    }

}
