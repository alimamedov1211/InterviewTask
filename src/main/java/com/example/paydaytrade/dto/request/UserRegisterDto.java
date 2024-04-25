package com.example.paydaytrade.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


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
