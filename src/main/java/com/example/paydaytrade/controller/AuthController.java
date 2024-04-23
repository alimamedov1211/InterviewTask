package com.example.paydaytrade.controller;

import com.example.paydaytrade.dto.request.AuthRequestDto;
import com.example.paydaytrade.dto.request.UserRegisterDto;
import com.example.paydaytrade.dto.response.AuthResponseDto;
import com.example.paydaytrade.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/refreshToken")
    public AuthResponseDto refreshToken(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        return authService.refreshToken(token);
    }

    @PostMapping("/register")
    public AuthResponseDto registration(@RequestBody UserRegisterDto request) {
        return authService.registration(request);
    }

    @PostMapping
    public AuthResponseDto authentication(@RequestBody AuthRequestDto request) {
        return authService.authentication(request);
    }

    @GetMapping("/confirm/{uuid}")
    public ResponseEntity<String> confirmation(@PathVariable UUID uuid){
        return authService.confirmAccount(uuid);
    }
}
