package com.example.paydaytrade.exceptions;


import com.example.paydaytrade.enums.Exceptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ExceptionResponse> handle(ApplicationException applicationException){
        Exceptions exception = applicationException.getExceptions();

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

}
