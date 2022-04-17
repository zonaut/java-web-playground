package com.zonaut.sbreactive.controllers;

import com.zonaut.sbreactive.common.exceptions.DuplicateFieldException;
import com.zonaut.sbreactive.controllers.TransferObjects.ValidationError;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class ControllerValidationHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<ValidationError>> handleException(WebExchangeBindException e) {
        e.getBindingResult()
                .getAllErrors()
                .forEach(objectError -> {
                    log.error(objectError.getObjectName());
                });

        var errors = e.getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<List<ValidationError>> handleException(DuplicateFieldException e) {
        ValidationError validationError = new ValidationError(e.getField(), e.getError());
        return ResponseEntity.badRequest().body(List.of(validationError));
    }

}
