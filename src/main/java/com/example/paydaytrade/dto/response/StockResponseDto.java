package com.example.paydaytrade.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseDto {
    String symbol;
    String name;
    String currency;
    String exchange;
    String mic_code;
    String country;
    String type;
    int price;
}
