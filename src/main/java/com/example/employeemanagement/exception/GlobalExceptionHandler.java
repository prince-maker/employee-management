package com.example.employeemanagement.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(ResponseStatusException.class)
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(EmployeeNotFoundException ex) {
      /*  ErrorResponse error=new ErrorResponse(ex.getReason(),ex.getStatusCode().value(),System.currentTimeMillis());
        return new ResponseEntity<>(error, ex.getStatusCode());*/
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(
            MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                400,
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
