package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.request.AuthRequestDto;
import com.example.paydaytrade.dto.request.UserRegisterDto;
import com.example.paydaytrade.dto.response.AuthResponseDto;
import com.example.paydaytrade.entity.ConfirmToken;
import com.example.paydaytrade.entity.Jwt;
import com.example.paydaytrade.entity.Role;
import com.example.paydaytrade.entity.User;
import com.example.paydaytrade.enums.RolesEnum;
import com.example.paydaytrade.repository.RoleRepository;
import com.example.paydaytrade.repository.TokenRepository;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.service.IAuthService;
import com.example.paydaytrade.service.security.JwtService;
import com.example.paydaytrade.service.security.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final SecurityHelper securityHelper;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final ConfirmTokenService confirmationTokenService;

    @Override
    public AuthResponseDto registration(UserRegisterDto request) {
        Role role = roleRepository.findRoleByName(RolesEnum.USER)
                .orElseThrow(() -> new RuntimeException("Role not found! "));

        User user = getUser(request, role);

        String accessToken = jwtService.generateJwt(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Jwt jwt = getToken(request, accessToken);
        tokenRepository.save(jwt);
        user.setJwt(jwt);
        userRepository.save(user);

        return AuthResponseDto.builder()
                .message("A confirmation email was sent to " + user.getMail() + ". Please verify your account!")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponseDto authentication(AuthRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }

        Optional<User> user = userRepository.findUserByUsernameOrMail(request.getUsername());
        String accessToken = jwtService.generateJwt(user.orElseThrow());
        String refreshToken = jwtService.generateRefreshToken(user.orElseThrow());

        Jwt token = tokenRepository.findJwtByUser(user.orElseThrow())
                .orElseThrow(() -> new RuntimeException("Token doesn't exist: " + user));
        token.setJwt(accessToken);
        tokenRepository.save(token);

        return AuthResponseDto.builder()
                .message(user.orElseThrow().getMail() + " login is successfully")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponseDto refreshToken(String authHeader) {
        if (!securityHelper.authHeaderIsValid(authHeader)) {
            throw new RuntimeException();
        }

        String jwt = authHeader.substring(7);
        String username = jwtService.extractUserName(jwt);

        if (username != null) {
            User user = userRepository.findUserByUsernameOrMail(username)
                    .orElseThrow(() -> new RuntimeException("Username doesn't exist: " + username));
            Jwt token = tokenRepository.findJwtByUser(user)
                    .orElseThrow(() -> new RuntimeException("Token doesn't exist: " + user));

            if (jwtService.isJwtValid(jwt, user)) {
                String accessToken = jwtService.generateJwt(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                token.setJwt(accessToken);
                tokenRepository.save(token);
                return AuthResponseDto.builder()
                        .message(username + "refreshing is successfully")
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }

        }

        throw new RuntimeException("Token is invalid!");
    }

    private User getUser(UserRegisterDto request, Role role) {
        User user = User.builder()
                .mail(request.getMail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);


        ConfirmToken token = ConfirmToken.builder()
                .jwt(UUID.randomUUID().toString())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        confirmationTokenService.save(token);
        mailSenderService.sendMail(request.getMail(), token);
        return user;
    }

    private Jwt getToken(UserRegisterDto request, String accessToken) {
        Optional<User> _user = userRepository.findUserByUsernameOrMail(request.getUsername());

        Jwt token = Jwt.builder()
                .jwt(accessToken)
                .isRevoked(false)
                .isExpired(false)
                .user(_user.orElseThrow())
                .createdAt(LocalDateTime.now())
                .build();
        return token;
    }

    public ResponseEntity<String> confirmAccount(UUID uuid) {
        ConfirmToken jwt = confirmationTokenService.getTokenByUUID(uuid.toString());
        if (jwt != null) {
            User user = jwt.getUser();
            user.setEnable(true);
            userRepository.save(user);

            jwt.setConfirmedAt(LocalDateTime.now());
            confirmationTokenService.save(jwt);
            return ResponseEntity.ok().body(user.getMail() + " Confirmation is successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Link is invalid");
        }

    }

}
