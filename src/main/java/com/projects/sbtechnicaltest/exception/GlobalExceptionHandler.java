package com.projects.sbtechnicaltest.exception;

import com.projects.sbtechnicaltest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleStudentAlreadyExistsException(StudentAlreadyExistsException ex) {
        log.warn("Estudiante duplicado: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .error("STUDENT_ALREADY_EXISTS")
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(WebExchangeBindException ex) {
        log.warn("Error de validación: {}", ex.getMessage());
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(errors.toString())
                .error("VALIDATION_ERROR")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error no esperado: ", ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Error interno del servidor")
                .error("INTERNAL_SERVER_ERROR")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}