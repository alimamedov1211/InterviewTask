package com.example.paydaytrade.repository;

import com.example.paydaytrade.entity.ConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken,Long> {
    Optional<ConfirmToken> findConfirmTokenByJwt(String jwt);
}

