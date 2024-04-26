package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.request.IncreaseBalanceRequestDto;
import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.entity.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    ResponseEntity<GeneralResponseDto> increaseBalance(IncreaseBalanceRequestDto requestDto);

}
