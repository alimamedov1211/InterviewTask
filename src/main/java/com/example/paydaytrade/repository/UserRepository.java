package com.example.paydaytrade.repository;

import com.example.paydaytrade.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from _user where username = :username or mail = :username", nativeQuery = true)
    Optional<User> findUserByUsernameOrMail(@Param("username") String username);
}
