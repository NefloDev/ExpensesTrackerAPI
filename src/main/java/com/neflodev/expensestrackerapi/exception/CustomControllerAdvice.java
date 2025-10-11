package com.neflodev.expensestrackerapi.exception;

import com.neflodev.expensestrackerapi.dto.CustomExceptionResponse;
import com.neflodev.expensestrackerapi.exception.custom.CustomException;
import com.neflodev.expensestrackerapi.exception.custom.UsernameInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(UsernameInUseException.class)
    public ResponseEntity<CustomExceptionResponse> dataExistsExceptionHandling(CustomException exception){
        CustomExceptionResponse response = new CustomExceptionResponse(exception.getMessage(), HttpStatus.CONFLICT.value(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}
