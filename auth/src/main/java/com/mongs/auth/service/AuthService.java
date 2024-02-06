package com.mongs.auth.service;

import com.mongs.auth.dto.response.LoginResDto;
import com.mongs.auth.entity.Member;
import com.mongs.auth.entity.Token;
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
        /* 회원 가입 (회원 정보가 존재하지 않을 경우) */
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> this.registerMember(email, name));

        /* 엑세스 및 리프래시 토큰 발급 */
        Token token = Token.builder()
                .refreshToken(tokenProvider.generateRefreshToken())
                .deviceId(deviceId)
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .expiration(expiration)
                .build();
        token = tokenRepository.save(token);

        return LoginResDto.builder()
                .accessToken(tokenProvider.generateAccessToken(member.getId(), deviceId))
                .refreshToken(token.getRefreshToken())
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
