package com.example.paydaytrade.service.security;

import com.example.paydaytrade.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class SecurityHelper {

    public boolean servletPathIsAuth(@NonNull HttpServletRequest httpServletRequest) {
        return httpServletRequest.getServletPath().contains("/auth");
    }

    public boolean tokenIsDead(User user) {
        return user.getJwt().isExpired() || user.getJwt().isRevoked();
    }

    public boolean authHeaderIsValid(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    public boolean isJwtUsedFirst(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

}
