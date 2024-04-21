package com.example.paydaytrade.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ConfirmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String jwt;
    LocalDateTime createdAt;
    LocalDateTime confirmedAt;

    @OneToOne
    User user;

}
