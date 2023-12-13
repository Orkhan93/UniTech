package az.spring.exception.handler;

import az.spring.exception.EmailFailureException;
import az.spring.exception.EmailNotFoundException;
import az.spring.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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

}