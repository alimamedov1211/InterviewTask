package com.example.paydaytrade.controller;

import com.example.paydaytrade.dto.request.BuyAndSellStockDto;
import com.example.paydaytrade.dto.response.GeneralResponseDto;
import com.example.paydaytrade.entity.Stock;
import com.example.paydaytrade.service.IStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("stocks")
public class StockController {

    private final IStockService stockService;

    @GetMapping("/stock")
    public List<Stock> getAllStock(){
        return stockService.getAllStocks();
    }

    @PostMapping("/buyStock")
    public ResponseEntity<GeneralResponseDto> buyStock(@RequestBody BuyAndSellStockDto request){
        return ResponseEntity.ok().body(stockService.buyStock(request));
    }

    @PostMapping("/sellStock")
    public ResponseEntity<GeneralResponseDto> sellStock(@RequestBody BuyAndSellStockDto request){
        return ResponseEntity.ok().body(stockService.sellStock(request));
    }

}
