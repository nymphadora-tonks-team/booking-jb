package com.bootcamp.demo.restapi.exceptionhandler;

import com.bootcamp.demo.service.exception.ItemNotFoundException;
import com.bootcamp.demo.service.exception.ServiceException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        String errorMessage = String.join(", ", errors);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternally(new ApiError(errorMessage, status.value()), status);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    private ResponseEntity<ApiError> handleNoHandlerFoundException(WebRequest request) {
        String errorMessage = "Invalid URI: " + request.getContextPath();
        HttpStatus status = HttpStatus.NOT_FOUND;
        return handleExceptionInternally(new ApiError(errorMessage, status.value()), status);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ApiError> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String errorMessage = ex.getMethod() + " method not supported";
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        return handleExceptionInternally(new ApiError(errorMessage, status.value()), status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ApiError> handleHttpMessageNotReadableException() {
        String errorMessage = "Json couldn't been parsed.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternally(new ApiError(errorMessage, status.value()), status);
    }

    @ExceptionHandler(TypeMismatchException.class)
    private ResponseEntity<ApiError> handleTypeMismatchException(TypeMismatchException ex) {
        String errorMessage = ex.getValue() + " has invalid type.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternally(new ApiError(errorMessage, status.value()), status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = ex.getValue() + " has invalid type.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternally(new ApiError(errorMessage, status.value()), status);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiError> handleException(Exception ex) {
        // Converting the stacktrace to string and writing to the log file
        LOGGER.error(ExceptionUtils.getStackTrace(ex));
        String errorMessage = "Oops. Something went wrong.";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternally(new ApiError(errorMessage, status.value()), status);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    private ResponseEntity<ApiError> handleItemNotFoundException(ItemNotFoundException ex) {
        String errorMessage = ex.getMessage().equals("") ? "Failed to find item" : ex.getMessage();
        HttpStatus status = HttpStatus.NOT_FOUND;
        return handleExceptionInternally(new ApiError(errorMessage, status.value()), status);
    }

    @ExceptionHandler(ServiceException.class)
    private ResponseEntity<ApiError> handleServiceException() {
        String errorMessage = "Oops. Something went wrong";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternally(new ApiError(errorMessage, status.value()), status);
    }

    private ResponseEntity<ApiError> handleExceptionInternally(ApiError body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }
}
