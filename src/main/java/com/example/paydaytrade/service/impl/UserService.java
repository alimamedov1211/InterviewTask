package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.request.IncreaseBalanceRequestDto;
import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.entity.User;
import com.example.paydaytrade.enums.Exceptions;
import com.example.paydaytrade.exceptions.ApplicationException;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public ResponseEntity<GeneralResponseDto> increaseBalance(IncreaseBalanceRequestDto requestDto) {
        Optional<User> user = userRepository.findUserByUsernameOrMail(requestDto.getUsername());
        if(encoder.matches(requestDto.getPassword(),user.orElseThrow(()-> new ApplicationException(Exceptions.WRONG_PASSWORD_OR_USERNAME_EXCEPTION)).getPassword())){
        user.orElseThrow().setWallet(user.orElseThrow().getWallet()+requestDto.getPrice());
        userRepository.save(user.orElseThrow());
        return ResponseEntity.ok(new GeneralResponseDto("Increasing " + requestDto.getPrice() + " manat to wallet is done successfully!"));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GeneralResponseDto("Username or Password is wrong!"));
        }
    }
}
