package com.example.paydaytrade.repository;

import com.example.paydaytrade.entity.Role;
import com.example.paydaytrade.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(RolesEnum rolesEnum);
}
