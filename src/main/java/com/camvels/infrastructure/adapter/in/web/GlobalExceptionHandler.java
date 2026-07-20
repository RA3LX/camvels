package com.camvels.infrastructure.adapter.in.web;

import com.camvels.domain.exception.DomainException;
import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handleDomain(DomainException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleStatus(ResponseStatusException ex) {
        logger.warn("ResponseStatusException: {}", ex.getReason(), ex);
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason() != null ? ex.getReason() : "Error"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos en la solicitud");
        logger.warn("Validation error: {}", message, ex);
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }

    @ExceptionHandler({IllegalArgumentException.class, ClassCastException.class, NumberFormatException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(Exception ex) {
        logger.warn("Bad request: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage() != null ? ex.getMessage() : "Datos inválidos en la solicitud"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        Throwable cause = ex.getCause();
        while (cause != null) {
            if (cause instanceof DomainException domainEx) {
                return ResponseEntity.badRequest().body(Map.of("error", domainEx.getMessage()));
            }
            if (cause instanceof SQLException sqlEx) {
                logger.error("Database error: {}", sqlEx.getMessage(), sqlEx);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Error de base de datos: " + sqlEx.getMessage()));
            }
            cause = cause.getCause();
        }
        logger.error("Unhandled exception", ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "Error interno del servidor";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", message));
    }
}
