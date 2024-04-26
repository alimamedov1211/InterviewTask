package com.example.paydaytrade.service.security;

import com.example.paydaytrade.entity.User;
import com.example.paydaytrade.repository.JwtRepository;
import com.example.paydaytrade.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SecurityHelper securityHelper;
    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        if (securityHelper.servletPathIsAuth((HttpServletRequest) request) || !securityHelper.authHeaderIsValid(authHeader)){
            filterChain.doFilter(request,response);
            return;
        }

        String jwt=authHeader.substring(7);
        String username=jwtService.extractUserName(jwt);

        jwtRepository.findJwtByJwt(jwt)
                .orElseThrow(() -> new RuntimeException("Token doesn't exist: " + jwt));

        if (securityHelper.isJwtUsedFirst(username)){
            User userDetails=userRepository.findUserByUsernameOrMail(username)
                    .orElseThrow(() -> new RuntimeException("Username doesn't exist: " + username));

            if (securityHelper.tokenIsDead(userDetails)){
                throw new RuntimeException("Token is dead");
            }
            if (jwtService.isJwtValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken userAuth=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                userAuth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails((jakarta.servlet.http.HttpServletRequest) request)
                );

                SecurityContextHolder.getContext().setAuthentication(userAuth);
            }
        }
        filterChain.doFilter(request,response);

    }
}
