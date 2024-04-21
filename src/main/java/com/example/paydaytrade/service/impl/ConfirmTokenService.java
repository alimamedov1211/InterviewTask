package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.entity.ConfirmToken;
import com.example.paydaytrade.enums.Exceptions;
import com.example.paydaytrade.exceptions.ApplicationException;
import com.example.paydaytrade.repository.ConfirmTokenRepository;
import com.example.paydaytrade.service.IConfirmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ConfirmTokenService implements IConfirmTokenService {
    private final ConfirmTokenRepository confirmTokenRepository;

    @Override
    public GeneralResponseDto save(ConfirmToken confirmationToken) {
        ConfirmToken confirmToken = confirmTokenRepository.save(confirmationToken);
        if (confirmToken != null) {
            return new GeneralResponseDto("Token is saved!");
        } else {
            throw new ApplicationException(Exceptions.TOKEN_INVALID_EXCEPTION);
        }
    }

    @Override
    public ConfirmToken getTokenByUUID(String uuid) {
        return confirmTokenRepository.findConfirmTokenByJwt(uuid)
                .orElseThrow(() -> new ApplicationException(Exceptions.TOKEN_NOT_FOUND_EXCEPTION));
    }
}
