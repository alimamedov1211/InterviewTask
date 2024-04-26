package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.request.BuyAndSellStockDto;
import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.entity.Stock;

import java.util.List;

public interface IStockService {

    Stock getStockByName(String name);

    List<Stock> getAllStocks();

    GeneralResponseDto buyStock(BuyAndSellStockDto offer);
    GeneralResponseDto sellStock(BuyAndSellStockDto offer);

}
