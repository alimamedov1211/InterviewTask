package com.example.paydaytrade.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyAndSellStockDto {

    @NotBlank(message = "StockId can't be empty!")
    private String stockId;

    @NotBlank(message = "Username can't be empty!")
    private String username;

    @Min(value=0,message = "Price can't be less than 0!")
    private double price;
}
