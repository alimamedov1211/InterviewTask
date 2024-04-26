package com.example.paydaytrade.repository;

import com.example.paydaytrade.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Long> {

    Optional<Stock> findStockByName(String name);

    Optional<Stock> findStockBySymbol(String symbol);

}
