package com.chanak.social.service;

import com.chanak.social.dto.AuthResponse;
import com.chanak.social.dto.LoginRequest;
import com.chanak.social.dto.RefreshRequest;
import com.chanak.social.dto.RegisterRequest;
import com.chanak.social.model.RefreshToken;
import com.chanak.social.model.User;
import com.chanak.social.repository.RefreshTokenRepository;
import com.chanak.social.repository.UserRepository;
import com.chanak.social.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    public AuthService(UserRepository userRepository,
                        RefreshTokenRepository refreshTokenRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        String displayName = request.getDisplayName() != null && !request.getDisplayName().isBlank()
                ? request.getDisplayName()
                : request.getUsername();

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                displayName
        );

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getId(), saved.getUsername());
        String refreshToken = createRefreshToken(saved);

        return new AuthResponse(token, refreshToken, saved.getId(), saved.getUsername(), saved.getDisplayName());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(request.getUsernameOrEmail()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        String refreshToken = createRefreshToken(user);

        return new AuthResponse(token, refreshToken, user.getId(), user.getUsername(), user.getDisplayName());
    }

    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken stored = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (stored.isExpired()) {
            refreshTokenRepository.delete(stored);
            throw new IllegalArgumentException("Refresh token expired, please login again");
        }

        User user = stored.getUser();
        String newAccessToken = jwtUtil.generateToken(user.getId(), user.getUsername());

        // Parehong refresh token pa rin ang ibabalik (hindi natin ito pinapalitan
        // sa bawat refresh call, para hindi na-i-invalidate agad ang session).
        return new AuthResponse(newAccessToken, stored.getToken(), user.getId(), user.getUsername(), user.getDisplayName());
    }

    private String createRefreshToken(User user) {
        // Tanggalin muna ang lumang refresh token ng user na ito, kung meron,
        // para isa lang ang "valid" na refresh token kada user sa isang pagkakataon.
        refreshTokenRepository.deleteByUser(user);

        String tokenValue = UUID.randomUUID().toString() + UUID.randomUUID();
        Instant expiry = Instant.now().plusMillis(refreshExpirationMs);

        RefreshToken refreshToken = new RefreshToken(tokenValue, user, expiry);
        refreshTokenRepository.save(refreshToken);

        return tokenValue;
    }
}
