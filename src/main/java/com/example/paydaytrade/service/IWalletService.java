package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.entity.User;

public interface IWalletService {

    GeneralResponseDto increaseWallet(double price, User user);
    GeneralResponseDto decreaseWallet(double price, User user);

    boolean checkBalance(double offerPrice, User user);
}
