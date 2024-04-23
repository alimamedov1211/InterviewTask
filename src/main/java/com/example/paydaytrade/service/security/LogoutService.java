package com.example.paydaytrade.service.security;

import com.example.paydaytrade.entity.Jwt;
import com.example.paydaytrade.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final SecurityHelper securityHelper;
    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        String authHeader = request.getHeader("Authorization");
        if (securityHelper.authHeaderIsValid(authHeader)) {
            String jwt = authHeader.substring(7);

            Optional<Jwt> tokenOptional = tokenRepository.findJwtByJwt(jwt);

            if (tokenOptional.isPresent()) {
                Jwt token = tokenOptional.get();
                token.setExpired(true);
                token.setRevoked(true);

                tokenRepository.save(token);

                SecurityContextHolder.clearContext();
            }
        }else {
            throw new RuntimeException("Authorization header is invalid");
        }

    }
}
