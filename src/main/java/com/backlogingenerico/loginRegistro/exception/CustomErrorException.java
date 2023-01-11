package com.backlogingenerico.loginRegistro.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class CustomErrorException extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // error handle for @Valid

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorDetails exceptionResponse = new ErrorDetails();
        exceptionResponse.setTimestamp(new Date());
        exceptionResponse.setStatus(status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        exceptionResponse.setMessages(errors);
        exceptionResponse.setMessage(!errors.isEmpty() ? String.join(", ", errors) : ex.toString());
        exceptionResponse.setDetails(request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public final ResponseEntity<ErrorDetails> handleNotFoundException(ApiException ex, WebRequest request) {
        ErrorDetails exceptionResponse = new ErrorDetails();
        exceptionResponse.setTimestamp(new Date());
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        exceptionResponse.setMessages(Collections.singletonList(ex.getMessage()));
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setDetails(request.getDescription(false));

        return new ResponseEntity<ErrorDetails>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        ErrorDetails exceptionResponse = new ErrorDetails();
        exceptionResponse.setTimestamp(new Date());
        exceptionResponse.setStatus(HttpStatus.CONFLICT.value());

        exceptionResponse.setMessages(Collections.singletonList(exception.getMessage()));
        exceptionResponse.setMessage(exception.getMessage());
        exceptionResponse.setDetails(exception.getMostSpecificCause().getMessage());


        return new ResponseEntity(exceptionResponse, HttpStatus.CONFLICT);
    }


}
