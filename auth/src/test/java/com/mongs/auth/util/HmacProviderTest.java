package com.mongs.auth.util;

import com.mongs.core.vo.passport.PassportVo;
import com.mongs.core.vo.passport.PassportData;
import com.mongs.core.vo.passport.PassportAccount;
import com.mongs.core.utils.HmacProvider;
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
        Long accountId = 1L;
        String deviceId = "testDeviceId";
        String role = "NORMAL";

        PassportVo passportVO = PassportVo.builder()
                .data(PassportData.builder()
                        .account(PassportAccount.builder()
                                .id(accountId)
                                .deviceId(deviceId)
                                .email(email)
                                .name(name)
                                .loginCount(1)
                                .role(role)
                                .build())
                        .build())
                .build();

        // when
        Optional<String> expected = hmacProvider.generateHmac(passportVO.data());

        // then
        assertThat(expected.isPresent()).isNotNull();
    }

    @Test
    @DisplayName("위변조 된 데이터인 경우 false 를 반환한다.")
    void verifyHmacForgery() {
        // given
        String forgeryKey = "test-forgery-key/test-forgery-key/test-forgery-key";
        String email = "forgery@forgery.com";
        String name = "위변조_테스트";
        Long accountId = 2L;
        String deviceId = "testDeviceId";
        String role = "NORMAL";
        
        // 위변조 데이터 생성
        PassportVo passportVO = PassportVo.builder()
                .data(PassportData.builder()
                        .account(PassportAccount.builder()
                                .id(accountId)
                                .deviceId(deviceId)
                                .email(email)
                                .name(name)
                                .loginCount(1)
                                .role(role)
                                .build())
                        .build())
                .build();

        String passIntegrity = hmacProvider.generateHmac(passportVO.data(), forgeryKey)
                .orElse(null);

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