package az.spring.exception.handler;

import az.spring.constraint.validation.Password;
import az.spring.exception.*;
import az.spring.exception.IllegalArgumentException;
import az.spring.exception.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomException {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handlerUserAlreadyExistsException(UserAlreadyExistsException exception) {
        log.error("handlerUserAlreadyExistsException {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handlerUserNotFoundException(UserNotFoundException exception) {
        log.error("handlerUserNotFoundException {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handlerEmailFailureException(EmailFailureException exception) {
        log.error("handlerEmailFailureException {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handlerEmailNotFoundException(EmailNotFoundException exception) {
        log.error("handlerEmailNotFoundException {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handlerUserNotVerifiedException(UserNotVerifiedException exception) {
        log.error("handlerUserNotVerifiedException {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handlerIncorrectPasswordException(IncorrectPasswordException exception) {
        log.error("handlerIncorrectPasswordException {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handlerAccountAlreadyExistsException(AccountAlreadyExistsException exception) {
        log.error("handlerAccountAlreadyExistsException {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handlerAccountNotFoundException(AccountNotFoundException exception) {
        log.error("handlerAccountNotFoundException {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handlerIllegalArgumentException(IllegalArgumentException exception) {
        log.error("handlerIllegalArgumentException {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(MethodArgumentNotValidException exception) {

        String fieldName = exception.getBindingResult().getFieldError().getField();
        String message = exception.getBindingResult().getFieldError().getDefaultMessage();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.name());
        errorResponse.setMessage(fieldName + message);
        log.error("Validation error : {}", exception.getMessage());
        return errorResponse;
    }

}