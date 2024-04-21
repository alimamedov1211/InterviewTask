package com.example.paydaytrade.service.security;

import com.example.paydaytrade.entity.Jwt;
import com.example.paydaytrade.repository.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtRepository jwtRepository;

    @Value("${application.security.secret-key}")
    String secretKey;

    @Value("${application.security.access-token-expiration}")
    Long accessTokenExpirationTime;

    @Value("${application.security.refresh-token-expiration}")
    Long refreshTokenExpirationTime;

    public Claims extractAllClaims(String jwt) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> cllaimsResolver) {
        return cllaimsResolver.apply(extractAllClaims(jwt));
    }

    public String extractUserName(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    public String generateJwt(
            Map<String, Object> extractClaims,
            UserDetails userDetails,
            long accessTokenExpiration
    ) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateRefreshToken(
            Map<String,Object> extractClaims,
            UserDetails userDetails,
            long refreshTokenExpiration
    ){
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+refreshTokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwt(UserDetails userDetails){
        return generateJwt(new HashMap<>(),userDetails,accessTokenExpirationTime);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return generateJwt(new HashMap<>(),userDetails,refreshTokenExpirationTime);
    }

    public boolean isJwtExpired(String jwt){
        boolean before = extractExpiration(jwt).before(new Date());
        if (before){
            Optional<Jwt> _token = jwtRepository.findJwtByJwt(jwt);
            _token.orElseThrow().setExpired(true);
            jwtRepository.save(_token.orElseThrow());
        }
        return before;
    }

    public boolean isJwtValid(String jwt,UserDetails userDetails){
        final String username=extractUserName(jwt);

        return username.equals(userDetails.getUsername()) && !isJwtExpired(jwt);
    }
}
