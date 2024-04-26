package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.entity.User;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.service.IWalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WalletService implements IWalletService {

    private final UserRepository userRepository;
    @Override
    public GeneralResponseDto increaseWallet(double price, User user) {
        double wallet = user.getWallet();
        user.setWallet(wallet + price);
        return new GeneralResponseDto(price + " manat added your wallet!");
    }

    @Override
    public GeneralResponseDto decreaseWallet(double price, User user) {
        double wallet = user.getWallet();
        user.setWallet(wallet - price);
        userRepository.save(user);
        return new GeneralResponseDto(price + " manat decreased your wallet!");
    }

    @Override
    public boolean checkBalance(double offerPrice, User user) {
        userRepository.save(user);
        return user.getWallet() >= offerPrice;
    }
}
