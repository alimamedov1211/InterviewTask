package com.example.paydaytrade.controller;


import com.example.paydaytrade.dto.request.IncreaseBalanceRequestDto;
import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    IUserService userService;


    @PostMapping("/increaseBalance")
    public ResponseEntity<GeneralResponseDto> increaseBalance(@RequestBody IncreaseBalanceRequestDto request){
        return userService.increaseBalance(request);
    }

}
