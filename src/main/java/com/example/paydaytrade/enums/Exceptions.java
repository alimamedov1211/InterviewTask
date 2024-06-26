package com.example.paydaytrade.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Exceptions {
    TOKEN_INVALID_EXCEPTION(HttpStatus.BAD_REQUEST, "Token is invalid!"),
    TOKEN_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"Token not found!"),
    MESSAGE_NOT_SEND_EXCEPTION(HttpStatus.BAD_GATEWAY,"Error occured when sending mail!"),
    WRONG_PASSWORD_OR_USERNAME_EXCEPTION(HttpStatus.BAD_REQUEST,"Username or Password is wrong!"),
    USER_NOT_FOUND_EXCEPTION(HttpStatus.BAD_REQUEST,"User not found!"),
    STOCK_NOT_FOUND_EXCEPTION(HttpStatus.BAD_REQUEST,"Stock with provided id is not found!"),
    BALANCE_NOT_ENOUGH_EXCEPTION(HttpStatus.BAD_REQUEST,"Your balance is not enough!"),
    STOCK_STATUS_IS_FALSE_EXCEPTION(HttpStatus.BAD_REQUEST, "Stock buyStatus is false. You can't sell it!"),
    DUPLICATE_EMAIL_OR_USERNAME_EXCEPTION(HttpStatus.BAD_REQUEST,"There is already a user with the provided username in the system!");

    private final HttpStatus httpStatus;
    private final String message;
}
