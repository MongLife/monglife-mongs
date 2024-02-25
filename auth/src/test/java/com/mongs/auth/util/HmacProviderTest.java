package com.mongs.auth.util;

import com.mongs.core.passport.PassportVO;
import com.mongs.core.passport.PassportData;
import com.mongs.core.passport.PassportMember;
import com.mongs.core.util.HmacProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class HmacProviderTest {
    @Autowired
    private HmacProvider hmacProvider;

    @Test
    @DisplayName("Object 타입의 객체를 받아 hmac 서명을 생성한다.")
    void generateHmac() {
        // given
        String email = "test@test.com";
        String name = "테스트";
        Long memberId = 1L;

        PassportVO passportVO = PassportVO.builder()
                .data(PassportData.builder()
                        .member(PassportMember.builder()
                                .id(memberId)
                                .email(email)
                                .name(name)
                                .build())
                        .build())
                .build();

        // when
        Optional<String> expected = hmacProvider.generateHmac(passportVO.data());

        // then
        assertThat(expected.isPresent()).isNotNull();
    }

    @Test
    @DisplayName("위변조 되지 않은 데이터인 경우 true 를 반환한다.")
    void verifyHmac() {
        // given
        String integrity = "IvnD7Ll7zL/YNfcQNy4R4lpIUi+u61auvOxty0v34EI=";
        String email = "test@test.com";
        String name = "테스트";
        Long memberId = 1L;

        PassportVO passportVO = PassportVO.builder()
                .data(PassportData.builder()
                        .member(PassportMember.builder()
                                .id(memberId)
                                .email(email)
                                .name(name)
                                .build())
                        .build())
                .passportIntegrity(integrity)
                .build();

        // when
        boolean expected = hmacProvider.verifyHmac(passportVO.data(), passportVO.passportIntegrity());

        // then
        assertThat(expected).isTrue();
    }

    @Test
    @DisplayName("위변조 된 데이터인 경우 false 를 반환한다.")
    void verifyHmacForgery() {
        // given
        String forgeryKey = "test-forgery-key/test-forgery-key/test-forgery-key";
        String email = "forgery@forgery.com";
        String name = "위변조_테스트";
        Long memberId = 2L;
        
        // 위변조 데이터 생성
        PassportVO passportVO = PassportVO.builder()
                .data(PassportData.builder()
                        .member(PassportMember.builder()
                                .id(memberId)
                                .email(email)
                                .name(name)
                                .build())
                        .build())
                .build();

        String passIntegrity = hmacProvider.generateHmac(passportVO.data(), forgeryKey)
                .orElseGet(() -> null);

        passportVO = passportVO.toBuilder()
                .passportIntegrity(passIntegrity)
                .build();

        // when
        boolean expected = hmacProvider.verifyHmac(passportVO.data(), passportVO.passportIntegrity());

        // then
        assertThat(passIntegrity).isNotNull();
        assertThat(expected).isFalse();
    }
}