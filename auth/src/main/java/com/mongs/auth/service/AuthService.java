package com.mongs.auth.service;

import com.mongs.auth.dto.response.LoginResDto;
import com.mongs.auth.dto.response.ReissueResDto;
import com.mongs.auth.entity.Member;
import com.mongs.auth.entity.Token;
import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.exception.ParameterValidateException;
import com.mongs.auth.repository.MemberRepository;
import com.mongs.auth.repository.TokenRepository;
import com.mongs.auth.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long expiration;

    public LoginResDto login(String deviceId, String email, String name) throws RuntimeException {
        if (deviceId.isEmpty() || deviceId.isBlank()) {
            throw new ParameterValidateException("deviceId");
        }
        if (email.isEmpty() || email.isBlank()) {
            throw new ParameterValidateException("email");
        }
        if (name.isEmpty() || name.isBlank()) {
            throw new ParameterValidateException("name");
        }

        /* 회원 가입 (회원 정보가 없는 경우) */
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> this.registerMember(email, name));

        /* AccessToken 및 RefreshToken 발급 */
        Token token = Token.builder()
                .refreshToken(tokenProvider.generateRefreshToken())
                .deviceId(deviceId)
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .expiration(expiration)
                .build();
        token = tokenRepository.save(token);

        return LoginResDto.builder()
                .accessToken(tokenProvider.generateAccessToken(token.getMemberId(), token.getDeviceId()))
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public ReissueResDto reissue(String refreshToken) throws RuntimeException {
        /* RefreshToken Redis 존재 여부 확인 */
        Token token = tokenRepository.findById(refreshToken)
                .orElseThrow(() -> new AuthorizationException("RefreshToken is Expired"));

        tokenRepository.deleteById(refreshToken);

        /* AccessToken 및 RefreshToken 발급 */
        Token newToken = Token.builder()
                .refreshToken(tokenProvider.generateRefreshToken())
                .deviceId(token.getDeviceId())
                .memberId(token.getMemberId())
                .createdAt(LocalDateTime.now())
                .expiration(expiration)
                .build();
        newToken = tokenRepository.save(newToken);

        return ReissueResDto.builder()
                .accessToken(tokenProvider.generateAccessToken(newToken.getMemberId(), newToken.getDeviceId()))
                .refreshToken(newToken.getRefreshToken())
                .build();
    }

    private Member registerMember(String email, String name) {
        Member registerMember = Member.builder()
                .name(name)
                .email(email)
                .build();
        return memberRepository.save(registerMember);
    }
}
