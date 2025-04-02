package com.mongs.play.module.jwt.provider;

import com.mongs.play.module.jwt.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationTokenProvider {

    @Value("${env.jwt.access-expiration}")
    private Long ACCESS_TOKEN_EXPIRED;
    @Value("${env.jwt.refresh-expiration}")
    private Long REFRESH_TOKEN_EXPIRED;

    private final JwtTokenUtil jwtTokenUtil;

    public Long getRefreshTokenExpiration() {
        return REFRESH_TOKEN_EXPIRED;
    }

    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = jwtTokenUtil.extractAllClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException | SignatureException | IllegalArgumentException e) {
            return true;
        }
    }
    public String generateAccessToken(Long memberId, String deviceId) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        claims.put("deviceId", deviceId);
        return jwtTokenUtil.generateToken(claims, ACCESS_TOKEN_EXPIRED);
    }
    public String generateRefreshToken() {
        Claims claims = Jwts.claims();
        return jwtTokenUtil.generateToken(claims, REFRESH_TOKEN_EXPIRED);
    }
    public Optional<Long> getExpiredSeconds(String token) {
        if (isTokenExpired(token)) {
            return Optional.empty();
        }
        LocalDateTime expiration = jwtTokenUtil.extractAllClaims(token).getExpiration().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        long expired = Duration.between(LocalDateTime.now(), expiration).getSeconds();
        return Optional.of(Math.max(0, expired));
    }
    public Optional<Long> getMemberId(String token) {
        if (isTokenExpired(token)) {
            return Optional.empty();
        }
        return Optional.of(jwtTokenUtil.extractAllClaims(token).get("memberId", Long.class));
    }
    public Optional<String> getDeviceId(String token) {
        if (isTokenExpired(token)) {
            return Optional.empty();
        }
        return Optional.of(jwtTokenUtil.extractAllClaims(token).get("deviceId", String.class));
    }
}