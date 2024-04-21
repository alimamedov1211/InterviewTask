package com.example.paydaytrade.repository;

import com.example.paydaytrade.entity.Jwt;
import com.example.paydaytrade.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<Jwt,Long> {
    Optional<Jwt> findJwtByJwt(String jwt);
    Optional<Jwt> findJwtByUser(User user);

}
