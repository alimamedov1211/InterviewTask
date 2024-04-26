package com.example.paydaytrade.repository;

import com.example.paydaytrade.entity.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock,Long> {

    UserStock findUserStockBySymbol(String symbol);

}
