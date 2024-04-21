package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.request.AuthRequestDto;
import com.example.paydaytrade.dto.request.UserRegisterDto;
import com.example.paydaytrade.dto.response.AuthResponseDto;

public interface IAuthService {

    AuthResponseDto registration(UserRegisterDto request);
    AuthResponseDto authentication(AuthRequestDto request);
    AuthResponseDto refreshToken(String authHeader);

}
