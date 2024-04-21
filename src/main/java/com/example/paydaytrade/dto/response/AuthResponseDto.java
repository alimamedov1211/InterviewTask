package com.example.paydaytrade.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
    String message;
    String accessToken;
    String refreshToken;
}
