package com.mongs.auth.util;

import com.mongs.auth.passport.Passport;
import com.mongs.auth.passport.PassportMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class HmacProviderTest {
    @Autowired
    private HmacProvider hmacProvider;

    @Test
    @DisplayName("Object 타입의 객체를 받아 hmac 서명을 생성한다.")
    void generateHmac() throws Exception {
        // given
        String email = "test@test.com";
        String name = "테스트";
        Long memberId = 1L;

        Passport passport = Passport.builder()
                .member(PassportMember.builder()
                        .id(memberId)
                        .email(email)
                        .name(name)
                        .build())
                .build();

        // when
        String expected = hmacProvider.generateHmac(passport);

        // then
        assertThat(expected).isNotNull();
    }

    @Test
    @DisplayName("위변조 되지 않은 데이터인 경우 true 를 반환한다.")
    void verifyHmac() throws Exception {
        // given
        String integrity = "QJTRC/6S6Trbwyu2ThyuR4D/bFmamHNy/d9nIYGc68w=";
        String email = "test@test.com";
        String name = "테스트";
        Long memberId = 1L;

        Passport passport = Passport.builder()
                .member(PassportMember.builder()
                        .id(memberId)
                        .email(email)
                        .name(name)
                        .build())
                .passportIntegrity(integrity)
                .build();

        // when
        boolean expected = hmacProvider.verifyHmac(passport, passport.passportIntegrity());

        // then
        assertThat(expected).isTrue();
    }

    @Test
    @DisplayName("위변조 된 데이터인 경우 false 를 반환한다.")
    void verifyHmacForgery() throws Exception {
        // given
        String forgeryKey = "test-forgery-key/test-forgery-key/test-forgery-key";
        String email = "forgery@forgery.com";
        String name = "위변조_테스트";
        Long memberId = 2L;
        
        // 위변조 데이터 생성
        Passport passport = Passport.builder()
                .member(PassportMember.builder()
                        .id(memberId)
                        .email(email)
                        .name(name)
                        .build())
                .build();
        passport = passport.toBuilder()
                .passportIntegrity( hmacProvider.generateHmac(passport, forgeryKey))
                .build();

        // when
        boolean expected = hmacProvider.verifyHmac(passport, passport.passportIntegrity());

        // then
        assertThat(expected).isFalse();
    }
}