package com.example.paydaytrade.entity;

import com.example.paydaytrade.enums.RolesEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    RolesEnum name;
    @OneToMany(mappedBy = "role")
    List<User> users;

}
