package az.spring.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String code, String message) {
        super(message);
    }

}