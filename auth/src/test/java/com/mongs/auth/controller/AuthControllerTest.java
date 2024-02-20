package com.mongs.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.auth.dto.request.LoginReqDto;
import com.mongs.auth.dto.request.PassportReqDto;
import com.mongs.auth.dto.request.ReissueReqDto;
import com.mongs.auth.dto.response.LoginResDto;
import com.mongs.auth.dto.response.ReissueResDto;
import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.exception.AuthErrorCode;
import com.mongs.auth.exception.NotFoundException;
import com.mongs.auth.exception.PassportException;
import com.mongs.core.passport.PassportData;
import com.mongs.core.passport.PassportMember;
import com.mongs.auth.service.AuthService;
import com.mongs.core.passport.PassportVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("deviceId, email, name 으로 로그인하면 accessToken 과 refreshToken 을 반환 한다.")
    void login() throws Exception {
        // given
        String deviceId = "test-deviceId";
        String email = "test@test.com";
        String name = "test-name";
        String accessToken = "test-accessToken";
        String refreshToken = "test-refreshToken";

        LoginResDto loginResDto = LoginResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        when(authService.login(deviceId, email, name))
                .thenReturn(loginResDto);

        // when
        // Response
        ResultActions resultActions = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginReqDto(deviceId, email, name))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken));
        // Parameter
        var deviceIdCaptor = ArgumentCaptor.forClass(String.class);
        var emailCaptor = ArgumentCaptor.forClass(String.class);
        var nameCaptor = ArgumentCaptor.forClass(String.class);
        verify(authService, times(1)).login(deviceIdCaptor.capture(), emailCaptor.capture(), nameCaptor.capture());

        var passedDeviceId = deviceIdCaptor.getValue();
        var passedEmail = emailCaptor.getValue();
        var passedName = nameCaptor.getValue();
        assertThat(passedDeviceId).isEqualTo(deviceId);
        assertThat(passedEmail).isEqualTo(email);
        assertThat(passedName).isEqualTo(name);
    }

    @Test
    @DisplayName("deviceId 없이 로그인하면 INVALID_PARAMETER 에러 메시지를 반환한다.")
    void loginNotDeviceId() throws Exception {
        // given
        String email = "test@test.com";
        String name = "test-name";

        // when
        // Response
        ResultActions resultActions = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new LoginReqDto(null, email, name))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PARAMETER.getMessage()));
    }

    @Test
    @DisplayName("email 없이 로그인하면 INVALID_PARAMETER 에러 메시지를 반환한다.")
    void loginNotEmail() throws Exception {
        // given
        String deviceId = "test-deviceId";
        String name = "test-name";

        // when
        // Response
        ResultActions resultActions = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new LoginReqDto(deviceId, null, name))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PARAMETER.getMessage()));
    }

    @Test
    @DisplayName("email 형식이 맞지 않는 경우 INVALID_PARAMETER 에러 메시지를 반환한다.")
    void loginNotEmailFormat() throws Exception {
        // given
        String deviceId = "test-deviceId";
        String email = "test-test.com";
        String name = "test-name";

        // when
        // Response
        ResultActions resultActions = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new LoginReqDto(deviceId, email, name))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PARAMETER.getMessage()));
    }

    @Test
    @DisplayName("name 없이 로그인하면 INVALID_PARAMETER 에러 메시지를 반환한다.")
    void loginNotName() throws Exception {
        // given
        String deviceId = "test-deviceId";
        String email = "test@test.com";

        // when
        // Response
        ResultActions resultActions = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new LoginReqDto(deviceId, email, null))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PARAMETER.getMessage()));
    }

    @Test
    @DisplayName("refreshToken 으로 토큰 재발행하면 accessToken 과 refreshToken 을 반환한다.")
    void reissue() throws Exception {
        // given
        String refreshToken = "test-refreshToken";
        String newAccessToken = "test-new-accessToken";
        String newRefreshToken = "test-new-refreshToken";

        ReissueResDto reissueResDto = ReissueResDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        when(authService.reissue(refreshToken))
                .thenReturn(reissueResDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new ReissueReqDto(refreshToken))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.accessToken").value(newAccessToken))
                .andExpect(jsonPath("$.refreshToken").value(newRefreshToken));
        // Parameter
        var refreshTokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(authService, times(1)).reissue(refreshTokenCaptor.capture());
        var passedRefreshToken = refreshTokenCaptor.getValue();
        assertThat(passedRefreshToken).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("refreshToken 없이 토큰 재발행하면 INVALID_PARAMETER 에러 메시지를 반환한다.")
    void reissueNotRefreshToken() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new ReissueReqDto(null))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PARAMETER.getMessage()));
    }

    @Test
    @DisplayName("토큰 재발행 또는 재로그인 시, 기존 refreshToken 은 만료되고 REFRESH_TOKEN_EXPIRED 에러 메시지를 반환한다.")
    void reissuePastRefreshTokenExpired() throws Exception {
        // given
        String refreshToken = "test-refreshToken";

        when(authService.reissue(refreshToken))
                .thenThrow(new AuthorizationException(AuthErrorCode.REFRESH_TOKEN_EXPIRED));

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new ReissueReqDto(refreshToken))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.message").value(AuthErrorCode.REFRESH_TOKEN_EXPIRED.getMessage()));
    }

    @Test
    @DisplayName("accessToken 으로 Passport 를 발급받고 반환한다.")
    void passport() throws Exception {
        // given
        String passportIntegrity = "test-passportIntegrity";
        String email = "test@test.com";
        String name = "test-name";
        Long memberId = 1L;
        String accessToken = "test-accessToken";

        PassportVO passportVO = PassportVO.builder()
                .data(PassportData.builder()
                        .member(PassportMember.builder()
                                .id(memberId)
                                .email(email)
                                .name(name)
                                .build())
                        .build())
                .passportIntegrity(passportIntegrity)
                .build();

        when(authService.passport(accessToken))
                .thenReturn(passportVO);

        // when
        // Response
        ResultActions resultActions = mockMvc.perform(post("/auth/passport")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PassportReqDto(accessToken))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.data.member.id").value(memberId))
                .andExpect(jsonPath("$.data.member.email").value(email))
                .andExpect(jsonPath("$.data.member.name").value(name))
                .andExpect(jsonPath("$.passportIntegrity").value(passportIntegrity));
        // Parameter
        var accessTokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(authService, times(1)).passport(accessTokenCaptor.capture());

        var passedAccessToken = accessTokenCaptor.getValue();
        assertThat(passedAccessToken).isEqualTo(accessToken);
    }

    @Test
    @DisplayName("만료된 accessToken 으로 Passport 를 발급받으면 ACCESS_TOKEN_EXPIRED 에러 메시지를 반환한다.")
    void passportAccessTokenExpired() throws Exception {
        // given
        String accessToken = "test-accessToken";

        when(authService.passport(accessToken))
                .thenThrow(new AuthorizationException(AuthErrorCode.ACCESS_TOKEN_EXPIRED));

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/passport")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PassportReqDto(accessToken))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.message").value(AuthErrorCode.ACCESS_TOKEN_EXPIRED.getMessage()));
    }

    @Test
    @DisplayName("accessToken 의 memberId 를 통해 회원 정보를 조회할 수 없는 경우 MEMBER_NOT_FOUND 에러 메시지를 반환한다.")
    void passportNotFoundMember() throws Exception {
        // given
        String accessToken = "test-accessToken";

        when(authService.passport(accessToken))
                .thenThrow(new NotFoundException(AuthErrorCode.MEMBER_NOT_FOUND));

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/passport")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PassportReqDto(accessToken))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.message").value(AuthErrorCode.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("Hmac 서명을 발행에 실패한 경우 PASSPORT_GENERATE_FAIL 에러 메시지를 반환한다.")
    void passportGenerateIntegrityFail() throws Exception {
        // given
        String accessToken = "test-accessToken";

        when(authService.passport(accessToken))
                .thenThrow(new PassportException(AuthErrorCode.PASSPORT_GENERATE_FAIL));

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/passport")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PassportReqDto(accessToken))));

        // then
        // HttpStatus & contentType
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // data
        resultActions
                .andExpect(jsonPath("$.message").value(AuthErrorCode.PASSPORT_GENERATE_FAIL.getMessage()));
    }
}
