package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.request.AuthRequestDto;
import com.example.paydaytrade.dto.request.UserRegisterDto;
import com.example.paydaytrade.dto.response.AuthResponseDto;
import org.apache.coyote.BadRequestException;

public interface IAuthService {

    AuthResponseDto registration(UserRegisterDto request) throws BadRequestException;
    AuthResponseDto authentication(AuthRequestDto request);
    AuthResponseDto refreshToken(String authHeader);

}
