package com.wex.transactions.exchange.rates.infra;

import com.wex.transactions.exchange.rates.dtos.ExceptionDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ContollerExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleConstraintViolation(ConstraintViolationException exception) {

        Set<ConstraintViolation<?>> violationsSet = exception.getConstraintViolations();
        List<String> messages = violationsSet.stream().map(ConstraintViolation::getMessage).toList();
        ExceptionDTO exceptionDTO = new ExceptionDTO("400", messages);

        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleConstraintViolation(HttpMessageNotReadableException exception) {

        String message = exception.getMessage();
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());
        ExceptionDTO exceptionDTO = new ExceptionDTO("400", messages);

        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGeneralException(Exception exception) {

        String message = exception.getMessage();
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());
        ExceptionDTO exceptionDTO = new ExceptionDTO("500", messages);

        return ResponseEntity.internalServerError().body(exceptionDTO);
    }

}
