package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.entity.ConfirmToken;

public interface IConfirmTokenService {
    GeneralResponseDto save(ConfirmToken confirmationToken);
    ConfirmToken getTokenByUUID(String uuid);
}
