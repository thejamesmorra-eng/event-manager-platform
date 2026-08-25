package dev.sorokin.eventmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Got validation exception ", e);

        String detailedMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                "Validation error",
                detailedMessage,
                LocalDateTime.now().format(formatter));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessageResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Got entity not found exception ", e);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                "Entity not found",
                e.getMessage(),
                LocalDateTime.now().format(formatter));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessageResponse);
    }

    @ExceptionHandler(LocationAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageResponse> handleLocationAlreadyExistsException(LocationAlreadyExistsException e) {
        log.error("Got location already exists exception ", e);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                "Location is already exists",
                e.getMessage(),
                LocalDateTime.now().format(formatter));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessageResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleException(Exception e) {
        log.error("Got server exception {}", e.getMessage(), e);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                "Server error",
                e.getMessage(),
                LocalDateTime.now().format(formatter));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessageResponse);
    }
}
