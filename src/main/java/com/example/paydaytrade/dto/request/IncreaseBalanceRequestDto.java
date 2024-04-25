package com.example.paydaytrade.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class IncreaseBalanceRequestDto {

    @NotBlank(message = "Username can't be empty!")
    String username;

    @NotBlank(message = "Password can't be empty!")
    @Size(min = 6, message = "Password can't be less than 6 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Password must be alphanumeric")
    String password;

    @Min(value = 0, message = "Price must be a positive value")
    int price;

}
