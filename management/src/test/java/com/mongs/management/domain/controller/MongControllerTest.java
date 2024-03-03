package com.mongs.management.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.management.code.TestMongCode;
import com.mongs.management.domain.mong.controller.MongController;
import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.repository.MongRepository;
import com.mongs.management.domain.security.WithMockPassportDetail;
import com.mongs.management.domain.mong.service.MongService;
import com.mongs.management.domain.mong.service.dto.Poop;
import com.mongs.management.domain.mong.service.dto.Sleep;
import com.mongs.management.domain.mong.service.dto.Training;
import com.mongs.management.domain.mong.service.dto.TrainingCount;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MongController.class)
@WithMockPassportDetail
public class MongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MongRepository mongRepository;

    @MockBean
    private MongService mongService;

    @Autowired
    private ObjectMapper objectMapper;
    private Mong mong;

    // 테스트 전 필요한 설정
    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String mongSleepTime = "22:00";
        String mongAwakeTime = "08:00";

        Boolean sleep = isSleep(mongSleepTime, mongAwakeTime);

        mong = Mong.builder()
                .memberId(1L)
                .name(TestMongCode.CH000.getName())
                .sleepTime(mongSleepTime)
                .wakeUpTime(mongAwakeTime)
                .weight(new Random().nextDouble() * 100)
                .numberOfPoop(10)
                .isSleeping(sleep)
                .build();

        when(mongRepository.findManagementByMemberId(1L)).thenReturn(Optional.of(mong));
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
        given(mongService.toCleanMongsPoop(memberId)).willReturn(poop);

        // When & Then
        mockMvc.perform(put("/management/poop")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("몽 배변 치우기"));

        var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(mongService, Mockito.times(1)).toCleanMongsPoop(memberIdCaptor.capture());

        var passedMemberId = memberIdCaptor.getValue();
        assertThat(passedMemberId).isEqualTo(memberId);
    }

    @Test
    public void mongTraining() throws Exception {
        // Given
        Long memberId = 1L;
        TrainingCount trainingCount = new TrainingCount(100);
        Training training = Training.of(mong);
        given(mongService.mongTraining(any(TrainingCount.class), eq(memberId))).willReturn(training);

        // When & Then
        mockMvc.perform(put("/management/training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingCount)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("몽 훈련"));

        var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        var trainingCaptor = ArgumentCaptor.forClass(TrainingCount.class);
        verify(mongService, Mockito.times(1))
                .mongTraining(trainingCaptor.capture(), memberIdCaptor.capture());

        assertThat(memberIdCaptor.getValue()).isEqualTo(memberId);
        assertThat(trainingCaptor.getValue().getTrainingCount()).isEqualTo(trainingCount.getTrainingCount());
    }

    @Test
    public void toCheckMongLifeTime() throws Exception {
        // Given
        Long memberId = 1L;
        Sleep sleep = Sleep.of(mong);
        given(mongService.toCheckMongsLifetime(memberId)).willReturn(sleep);

        // When & Then
        mockMvc.perform(put("/management/sleep/toggle")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("몽이 자는지 확인"));

        var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(mongService, Mockito.times(1))
                .toCheckMongsLifetime(memberIdCaptor.capture());

        assertThat(memberIdCaptor.getValue()).isEqualTo(memberId);
    }


    private Boolean isSleep(String sleepStart, String sleepEnd) {
        LocalTime startTime = LocalTime.parse(sleepStart, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(sleepEnd, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime currentTime = LocalTime.of(15, 0);

        if (endTime.isBefore(startTime)) {
            return !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime);
        } else {
            return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
        }
    }

}
