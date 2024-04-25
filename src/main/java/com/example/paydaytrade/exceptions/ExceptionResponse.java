package com.example.paydaytrade.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ExceptionResponse {
    private String message;
    private LocalDateTime timestamp;
}
