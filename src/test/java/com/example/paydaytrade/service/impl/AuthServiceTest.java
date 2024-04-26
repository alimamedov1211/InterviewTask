package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.request.AuthRequestDto;
import com.example.paydaytrade.dto.request.UserRegisterDto;
import com.example.paydaytrade.dto.response.AuthResponseDto;
import com.example.paydaytrade.entity.ConfirmToken;
import com.example.paydaytrade.entity.Jwt;
import com.example.paydaytrade.entity.Role;
import com.example.paydaytrade.entity.User;
import com.example.paydaytrade.enums.RolesEnum;
import com.example.paydaytrade.repository.JwtRepository;
import com.example.paydaytrade.repository.RoleRepository;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.service.IConfirmTokenService;
import com.example.paydaytrade.service.security.JwtService;
import com.example.paydaytrade.service.security.SecurityHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private SecurityHelper securityHelper;

    @Mock
    private JwtRepository jwtRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private ConfirmTokenService confirmTokenService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(
                userRepository,
                authenticationManager,
                roleRepository,
                jwtService,
                securityHelper,
                jwtRepository,
                encoder,
                mailSenderService,
                confirmTokenService
        );
    }

    @Test
    void testConfirmAccount() {
        UUID uuid = UUID.randomUUID();
        ConfirmToken token = ConfirmToken.builder()
                .jwt(uuid.toString())
                .user(new User())
                .build();
        when(confirmTokenService.getTokenByUUID(uuid.toString())).thenReturn(token);

        ResponseEntity<String> result = authService.confirmAccount(uuid);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("email@example.com Confirmation is successfully", result.getBody());
    }

    @Test
    void testRegistration() {
        UserRegisterDto registrationRequest = new UserRegisterDto("username", "password", "email@example.com");

        Role role = Role.builder()
                .name(RolesEnum.USER)
                .build();
        User user = User.builder()
                .username(registrationRequest.getUsername())
                .mail(registrationRequest.getMail())
                .role(role)
                .build();

        when(roleRepository.findRoleByName(RolesEnum.USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(encoder.encode(registrationRequest.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateJwt(any(User.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthResponseDto authResponseDto = authService.registration(registrationRequest);

        assertEquals("A confirmation email was sent to email@example.com. Please verify your account by clicking on the link ", authResponseDto.getMessage());
        assertEquals("accessToken", authResponseDto.getAccessToken());
        assertEquals("refreshToken", authResponseDto.getRefreshToken());
    }

    @Test
    void testAuthentication() {
        AuthRequestDto authenticationRequest = new AuthRequestDto("username", "password");
        User user = User.builder()
                .username(authenticationRequest.getUsername())
                .password(encoder.encode(authenticationRequest.getPassword()))
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findUserByUsernameOrMail(authenticationRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateJwt(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthResponseDto authResponseDto = authService.authentication(authenticationRequest);

        assertEquals("A confirmation email was sent to null. Please verify your account by clicking on the link ", authResponseDto.getMessage());
        assertEquals("accessToken", authResponseDto.getAccessToken());
        assertEquals("refreshToken", authResponseDto.getRefreshToken());
    }

    @Test
    void testRefreshToken() {
        String authHeader = "Bearer your-jwt-token";
        User user = User.builder()
                .username("username")
                .build();
        Jwt jwt = Jwt.builder()
                .jwt("oldAccessToken")
                .user(user)
                .build();

        when(securityHelper.authHeaderIsValid(authHeader)).thenReturn(true);
        when(jwtService.extractUserName("your-jwt-token")).thenReturn("username");
        when(userRepository.findUserByUsernameOrMail("username")).thenReturn(Optional.of(user));
        when(jwtService.isJwtValid("your-jwt-token", user)).thenReturn(true);
        when(jwtService.generateJwt(user)).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("newRefreshToken");
        when(jwtRepository.findJwtByUser(user)).thenReturn(Optional.of(jwt));

        AuthResponseDto authResponseDto = authService.refreshToken(authHeader);

        assertEquals("newAccessToken", authResponseDto.getAccessToken());
        assertEquals("newRefreshToken", authResponseDto.getRefreshToken());
    }
}
