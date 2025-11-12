package ru.yarigo.cerberus.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ProblemDetail> handleEntityExistsException(EntityExistsException ex, WebRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.CONFLICT, ex, "ENTITY_EXISTS", request);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.NOT_FOUND, ex, "ENTITY_NOT_FOUND", request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST, ex, "BAD_REQUEST", request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST, ex, "BAD_REQUEST", request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST, ex, "METHOD_ARGUMENT_TYPE_MISMATCH", request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST, ex, "INVALID_ARGUMENT", request);

        List<String> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        pd.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception ex, WebRequest request) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex, "INTERNAL_SERVER_ERROR", request);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

    private ProblemDetail buildProblemDetail(
            HttpStatus status,
            Exception ex,
            String errorCode,
            WebRequest request) {
        String message = ex.getMessage();
        String path = request.getDescription(false).replaceAll("uri=", "");
        Instant timestamp = Instant.now();

        log.error(message, ex);

        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle(status.getReasonPhrase());
        pd.setDetail(Optional.ofNullable(message).orElse("UnexpectedError"));
        pd.setProperty("errorCode", errorCode);
        pd.setProperty("timestamp", timestamp.toString());
        pd.setInstance(URI.create(path));

        return pd;
    }
}
