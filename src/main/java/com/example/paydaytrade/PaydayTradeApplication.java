package com.example.paydaytrade;

import com.example.paydaytrade.service.impl.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class PaydayTradeApplication implements CommandLineRunner {

    private final StockService stockService;

    public static void main(String[] args) {
        SpringApplication.run(PaydayTradeApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        //stockService.insertAllStockToDb();
    }
}
