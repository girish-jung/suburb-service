package com.auspos.suburb.controller;

import com.auspos.suburb.exception.DuplicateSuburbException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ DuplicateSuburbException.class, ValidationException.class, MethodArgumentNotValidException.class,
            InvalidFormatException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<String> handleDuplicateSuburbException(DuplicateSuburbException ex, HttpServletRequest request) {
        log.error("Suburb already exist in DB", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("Error occurred while processing request", ex);
        return new ResponseEntity<>("Error occurred while processing request", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
