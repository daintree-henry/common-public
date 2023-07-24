package online.devwiki.common.user.infrastructure.aop;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ControllerErrorAspect {
    private static final int STACK_TRACE_INDENTATION_COUNT = 4;
    private static final String STACK_TRACE_SPECIFIC_FIELD = "org.springframework.security";

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        printErrorMessageWithStackTrace("IllegalArgumentException", e);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        printErrorMessageWithStackTrace("HttpMessageNotReadableException", e);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<Object> handleBindException(BindException e) {
        printErrorMessageWithStackTrace("BindException", e);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        printErrorMessageWithStackTrace("MethodArgumentTypeMismatchException", e);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    public ResponseEntity<Object> handleDupKeyException(DuplicateKeyException e) {
        printErrorMessageWithStackTrace("DuplicateKeyException", e);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        printErrorMessageWithStackTrace("MethodArgumentNotValidException", e);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        printErrorMessageWithStackTrace("NullPointerException", e);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        printErrorMessageWithStackTrace("EntityNotFoundException", e);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleBusinessException(Exception e) {
        printErrorMessageWithStackTrace(e);

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getLocalizedMessage()
        );
        return new ResponseEntity<>(error, error.getStatus());
    }

    private void printErrorMessageWithStackTrace(Exception e){
        log.error(e.getMessage());
        if(e.getCause() != null)log.error(e.getCause().getMessage());
        log.error(Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .filter(line -> line.contains(STACK_TRACE_SPECIFIC_FIELD))
                .map(element -> " ".repeat(STACK_TRACE_INDENTATION_COUNT) + element)
                .collect(Collectors.joining("\n")));
    }

    private void printErrorMessageWithStackTrace(String className, Exception e){
        log.error("ERROR CLASS: ", e.getMessage());
        printErrorMessageWithStackTrace(e);
    }
}