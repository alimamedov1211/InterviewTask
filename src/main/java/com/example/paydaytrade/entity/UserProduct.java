package com.example.paydaytrade.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String symbol;
    String name;
    String currency;
    String exchange;
    String mic_code;
    String country;
    String type;
    int buyPrice;
    int sellPrice;
    @Builder.Default
    boolean buyStatus = false;
    @Builder.Default
    boolean sellStatus = false;
    @Builder.Default
    boolean sellRequest = false;

    @ManyToOne
    User user_;

}
