package com.example.paydaytrade.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
