package dev.sorokin.eventmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Got validation exception ", e);

        String detailedMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse("Validation error", detailedMessage));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("Got entity not found exception ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse("Entity not found", e.getMessage()));
    }

    @ExceptionHandler(LocationAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageResponse> handleLocationAlreadyExistsException(LocationAlreadyExistsException e) {
        log.warn("Got location already exists exception ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse("Location is already exists", e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessageResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("Got bad credentials exception ", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorResponse("Unauthorized", "Invalid login or password"));
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageResponse> handleLoginAlreadyExistsException(LoginAlreadyExistsException e) {
        log.warn("Got login already exists exception ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse("Login already exists", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleException(Exception e) {
        log.error("Got server exception {}", e.getMessage(), e);

        String detailedMessage = e.getMessage() != null
                ? e.getMessage()
                : "Unexpected server error";

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse("Server error", detailedMessage));
    }

    private ErrorMessageResponse buildErrorResponse(String message, String detailedMessage) {
        return new ErrorMessageResponse(
                message,
                detailedMessage,
                LocalDateTime.now().format(FORMATTER)
        );
    }
}
