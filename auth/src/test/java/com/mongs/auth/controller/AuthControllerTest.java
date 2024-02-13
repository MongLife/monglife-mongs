package com.mongs.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.auth.dto.request.LoginReqDto;
import com.mongs.auth.dto.response.LoginResDto;
import com.mongs.auth.exception.ErrorCode;
import com.mongs.auth.service.AuthService;
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
import org.testcontainers.shaded.org.apache.commons.lang3.ObjectUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
    @DisplayName("deviceId 없이 로그인하면 Invalid Parameter 에러 메시지를 반환한다.")
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
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_PARAMETER.getMessage()));
    }

    @Test
    @DisplayName("email 없이 로그인하면 Invalid Parameter 에러 메시지를 반환한다.")
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
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_PARAMETER.getMessage()));
    }

    @Test
    @DisplayName("email 형식이 맞지 않는 경우 Invalid Parameter 에러 메시지를 반환한다.")
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
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_PARAMETER.getMessage()));
    }

    @Test
    @DisplayName("name 없이 로그인하면 Invalid Parameter 에러 메시지를 반환한다.")
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
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_PARAMETER.getMessage()));
    }
}
