package com.example.paydaytrade.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserRegisterDto {

    @NotBlank(message = "Username can't be empty!")
    String username;

    @Email(message = "Invalid mail format!")
    String mail;

    @NotBlank(message = "password can;t be empty!")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Password must be alphanumeric!")
    String password;
}
