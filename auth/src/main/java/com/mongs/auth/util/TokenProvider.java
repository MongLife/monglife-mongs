package com.mongs.auth.util;

import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${application.security.jwt.secret-key}")
    private String JWT_KEY;

    @Value("${application.security.jwt.access-expiration}")
    private Long ACCESS_TOKEN_EXPIRED;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long REFRESH_TOKEN_EXPIRED;

    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = extractAllClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException | SignatureException e) {
            return true;
        }
    }
    public String generateAccessToken(Long memberId, String deviceId) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        claims.put("deviceId", deviceId);
        return generateToken(claims, ACCESS_TOKEN_EXPIRED);
    }
    public String generateRefreshToken() {
        Claims claims = Jwts.claims();
        return generateToken(claims, REFRESH_TOKEN_EXPIRED);
    }
    public Long getExpiredSeconds(String token) throws AuthorizationException {
        if (isTokenExpired(token)) {
            throw new AuthorizationException(ErrorCode.ACCESS_TOKEN_EXPIRED.getMessage());
        }
        LocalDateTime expiration = extractAllClaims(token).getExpiration().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        long expired = Duration.between(LocalDateTime.now(), expiration).getSeconds();
        return Math.max(0, expired);
    }
    public Long getMemberId(String token) throws AuthorizationException {
        if (isTokenExpired(token)) {
            throw new AuthorizationException(ErrorCode.ACCESS_TOKEN_EXPIRED.getMessage());
        }
        return extractAllClaims(token).get("memberId", Long.class);
    }
    public String getDeviceId(String token) throws AuthorizationException {
        if (isTokenExpired(token)) {
            throw new AuthorizationException(ErrorCode.ACCESS_TOKEN_EXPIRED.getMessage());
        }
        return extractAllClaims(token).get("deviceId", String.class);
    }

    private Claims extractAllClaims(String token) {
        Key signingKey = getSigningKey();
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = JWT_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(Claims claims, Long expired) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + expired * 1000);      // Millis Seconds
        Key signingKey = getSigningKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}