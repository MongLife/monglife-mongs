package com.mongs.auth.service;

import com.mongs.auth.dto.response.LoginResDto;
import com.mongs.auth.dto.response.PassportResDto;
import com.mongs.auth.passport.Passport;
import com.mongs.auth.dto.response.ReissueResDto;
import com.mongs.auth.entity.Member;
import com.mongs.auth.entity.Token;
import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.exception.ErrorCode;
import com.mongs.auth.exception.NotFoundException;
import com.mongs.auth.exception.PassportException;
import com.mongs.auth.passport.PassportMember;
import com.mongs.auth.repository.MemberRepository;
import com.mongs.auth.repository.TokenRepository;
import com.mongs.auth.util.HmacProvider;
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
    private final HmacProvider hmacProvider;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long expiration;

    public LoginResDto login(String deviceId, String email, String name) throws RuntimeException {
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
                .orElseThrow(() -> new AuthorizationException(ErrorCode.REFRESH_TOKEN_EXPIRED.getMessage()));

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
    
    public PassportResDto passport(String accessToken) throws RuntimeException {
        /* AccessToken 검증 */
        if (tokenProvider.isTokenExpired(accessToken)) {
            throw new AuthorizationException(ErrorCode.ACCESS_TOKEN_EXPIRED.getMessage());
        }

        Long memberId = tokenProvider.getMemberId(accessToken);
        
        /* AccessToken 의 memberId 로 member 조회 */
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        try {
            Passport passport = Passport.builder()
                    .member(PassportMember.builder()
                            .id(member.getId())
                            .email(member.getEmail())
                            .name(member.getName())
                            .build())
                    .build();

            String passportIntegrity = hmacProvider.generateHmac(passport);

            /* passport 생성 및 dto 반환 */
            return PassportResDto.builder()
                    .member(passport.member())
                    .passportIntegrity(passportIntegrity)
                    .build();
        } catch (Exception e) {
            throw new PassportException(ErrorCode.PASSPORT_GENERATE_FAIL.getMessage());
        }
    }
 
    private Member registerMember(String email, String name) {
        Member registerMember = Member.builder()
                .name(name)
                .email(email)
                .build();
        return memberRepository.save(registerMember);
    }
}
