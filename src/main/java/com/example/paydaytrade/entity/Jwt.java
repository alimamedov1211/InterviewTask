package com.example.paydaytrade.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
