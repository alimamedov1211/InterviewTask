package com.example.paydaytrade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Jwt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String jwt;
    boolean isRevoked;
    boolean isExpired;
    LocalDateTime createdAt;

    @OneToOne(mappedBy = "jwt")
    User user;

}
