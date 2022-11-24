package com.encom.msuser.exception;

import com.encom.msuser.configuration.annotation.LogExecutionTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHook extends DefaultErrorAttributes {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handle(Exception exception,
                                                      WebRequest request) {
        log.error("Uncaught exception: {}", exception.getStackTrace());
        return ofType(request, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, Object>> handle(SQLException exception,
                                                      WebRequest request) {
        log.error("Uncaught SQL exception: {}", exception.getStackTrace());
        return ofType(request, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Map<String, Object>> handle(JsonProcessingException exception,
                                                      WebRequest request) {
        log.error("Uncaught JsonProcessing exception: {}", exception.getStackTrace());
        return ofType(request, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException exception,
            WebRequest request) {
        log.error("Validation exception: {}", exception.getStackTrace());

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ofType(request, HttpStatus.BAD_REQUEST, errors.toString());
    }

    //  Custom exceptions

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handle(NotFoundException exception,
                                                      WebRequest request) {
        log.info("Data not found: {}", exception.getMessage());
        return ofType(request, HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handle(BadRequestException exception,
                                                      WebRequest request) {
        log.info("Invalid request: {}", exception.getMessage());
        return ofType(request, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(TokenExpirationException.class)
    public ResponseEntity<Map<String, Object>> handle(TokenExpirationException exception,
                                                      WebRequest request) {
        log.info("Error validating access token: {}", exception.getMessage());
        return ofType(request, HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    protected ResponseEntity<Map<String, Object>> ofType(WebRequest request, HttpStatus status, String message) {
        Map<String, Object> attributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        attributes.put("status", status.value());
        attributes.put("error", status.getReasonPhrase());
        attributes.put("message", message);
        attributes.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(attributes, status);
    }
}
