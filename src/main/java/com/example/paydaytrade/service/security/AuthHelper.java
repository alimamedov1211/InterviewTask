package com.example.paydaytrade.service.security;

import com.example.paydaytrade.entity.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AuthHelper {
    public Jwt buildJwt(Jwt jwt, String token){
        return Jwt.builder()
                .id(jwt!=null ? jwt.getId() : null)
                .jwt(token)
                .isExpired(false)
                .isRevoked(false)
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
