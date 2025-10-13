package com.neflodev.expensestrackerapi.exception;

import com.neflodev.expensestrackerapi.dto.general.CustomExceptionResponse;
import com.neflodev.expensestrackerapi.exception.custom.NotFoundException;
import com.neflodev.expensestrackerapi.exception.custom.ConflictException;
import com.neflodev.expensestrackerapi.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class CustomControllerAdvice {

    private static final Marker ERROR_MARKER = MarkerFactory.getMarker("ERROR");

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponse> exceptionHandling(CustomException exception){
        log.error(ERROR_MARKER, "com.neflodev.expensestrackerapi.exception.CustomControllerAdvice >> Expected Error Caught", exception);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = exception.getMessage();
        if (exception instanceof ConflictException){
            status = HttpStatus.CONFLICT;
        } else if (exception instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(new CustomExceptionResponse(message, status.value(), LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionResponse> unexpectedExceptionHandling(Exception exception){
        log.error(ERROR_MARKER, "com.neflodev.expensestrackerapi.exception.CustomControllerAdvice >> Unexpected Error Caught", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomExceptionResponse("An unexpected error ocurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now()));
    }

}
