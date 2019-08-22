package com.intuit.vkamble.biztrack.payment.handlers;

import exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.ConnectException;
import java.util.Date;

@ControllerAdvice //Enable this -> 400 error with no response payload. Alternative, @ResponseStatus annotation with custom exceptions
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        CustomError customError = new CustomError(new Date(), ex.getMessage(), request.getDescription(false), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidOperationException.class})
    protected ResponseEntity<Object> handleInvalidOperationErrorException(InvalidOperationException ex, WebRequest request) {
        CustomError customError = new CustomError(new Date(), ex.getMessage(), request.getDescription(false), String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConnectException.class})
    protected ResponseEntity<Object> handleConnectException(ConnectException ex, WebRequest request) {
        CustomError customError = new CustomError(new Date(), ex.getMessage(), request.getDescription(false), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return new ResponseEntity<>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {BusinessRuleViolationException.class})
    protected ResponseEntity<Object> handleConnectException(BusinessRuleViolationException ex, WebRequest request) {
        CustomError customError = new CustomError(new Date(), ex.getMessage(), request.getDescription(false), String.valueOf(HttpStatus.CONFLICT.value()));
        return new ResponseEntity<>(customError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {InternalException.class})
    protected ResponseEntity<Object> handleConnectException(InternalException ex, WebRequest request) {
        CustomError customError = new CustomError(new Date(), ex.getMessage(), request.getDescription(false), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return new ResponseEntity<>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ServiceUnavailableException.class})
    protected ResponseEntity<Object> handleConnectException(ServiceUnavailableException ex, WebRequest request) {
        CustomError customError = new CustomError(new Date(), ex.getMessage(), request.getDescription(false), String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
        return new ResponseEntity<>(customError, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomError customError = new CustomError(new Date(), "Validation error.", request.getDescription(false), String.valueOf(HttpStatus.BAD_REQUEST.value()));
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            customError.setCauses(fieldName, errorMessage);
        });
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

}
