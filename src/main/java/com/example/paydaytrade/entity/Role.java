package com.example.paydaytrade.entity;

import com.example.paydaytrade.enums.RolesEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    RolesEnum name;
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    List<User> users;

}
