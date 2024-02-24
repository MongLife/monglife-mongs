package com.mongs.management.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.management.domain.entity.Management;
import com.mongs.management.domain.repository.ManagementRepository;
import com.mongs.management.domain.security.WithMockPassportDetail;
import com.mongs.management.domain.service.ManagementService;
import com.mongs.management.domain.service.dto.Poop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ManagementController.class)
@WithMockPassportDetail
public class ManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagementRepository managementRepository;

    @MockBean
    private ManagementService managementService;

    @Autowired
    private ObjectMapper objectMapper;
    private Management mong;

    // 테스트 전 필요한 설정
    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mong = Management.builder()
                .memberId(1L)
                .name("Mong")
                .sleepStart(LocalDateTime.now().minusHours(1))
                .sleepEnd(LocalDateTime.now().plusHours(8))
                .weight(new Random().nextDouble() * 100)
                .poopCount(10)
                .sleep(true)
                .build();

        when(managementRepository.findManagementByMemberId(1L)).thenReturn(Optional.of(mong));
    }

    @Test
    public void testCreateMong() {

    }

    @Test
    public void testToMongStroke() {
    }

    @Test
    public void toCleanMongsPoop() throws Exception {
        // Given
        Long memberId = 1L;
        Poop poop = Poop.of(mong);
        given(managementService.toCleanMongsPoop(memberId)).willReturn(poop);

        // When & Then
        mockMvc.perform(put("/management/poop")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("몽 배변 치우기"));

        var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(managementService, Mockito.times(1)).toCleanMongsPoop(memberIdCaptor.capture());

        var passedMemberId = memberIdCaptor.getValue();
        assertThat(passedMemberId).isEqualTo(memberId);
    }

}
