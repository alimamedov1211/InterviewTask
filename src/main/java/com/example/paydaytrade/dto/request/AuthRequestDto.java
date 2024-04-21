package com.example.paydaytrade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {

    @NotBlank(message = "Username can't be empty!")
    String username;

    @NotBlank(message = "Password can't be empty!")
    @Size(min = 6 , message = "Password can't be less than 6 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Password must be alphanumeric!")
    String password;
}
