package study.springexception.exhandler.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.springexception.api.ApiExceptionV2Controller;
import study.springexception.exception.UserException;
import study.springexception.exhandler.ErrorResult;

@Slf4j
@RestControllerAdvice(assignableTypes = {ApiExceptionV2Controller.class})
public class ExControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult illegalArgExHandler(IllegalArgumentException ex) {
        log.warn("[illegalArgExHandler] ex", ex);
        return new ErrorResult("BAD", ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException ex) {
        log.warn("[userExHandler] ex", ex);
        return new ResponseEntity<>(new ErrorResult("USER-EX", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult exHandler(Exception ex) {
        log.warn("[exHandler] ex", ex);
        return new ErrorResult("EX", "Internal error");
    }
}
