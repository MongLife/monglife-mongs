package com.mongs.management.domain.service;

import com.mongs.management.domain.mong.entity.Management;
import com.mongs.management.domain.mong.repository.ManagementRepository;
import com.mongs.management.domain.mong.service.ManagementService;
import com.mongs.management.domain.mong.service.dto.InitMong;
import com.mongs.management.domain.mong.service.dto.TrainingCount;
import com.mongs.management.exception.ManagementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ManagementServiceTest {

    @MockBean
    private ManagementRepository managementRepository;

    @Autowired
    private ManagementService managementService;

    private InitMong initMong;
    private Long memberId;
    private Management mong;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        initMong = new InitMong("MongName", LocalDateTime.of(2024, 2, 5, 22, 0), LocalDateTime.of(2024, 2, 26, 8, 0));

        String sleepTimeStart = timeConverter(initMong.sleepStart());
        String sleepTimeEnd = timeConverter(initMong.sleepEnd());
        Boolean sleep = isSleep(sleepTimeStart, sleepTimeEnd);

        mong = Management.builder()
                .memberId(memberId)
                .name(initMong.name())
                .sleepStart(sleepTimeStart)
                .sleepEnd(sleepTimeEnd)
                .weight(new Random().nextDouble() * 100)
                .poopCount(10)
                .sleep(sleep)
                .build();

        when(managementRepository.findManagementByMemberId(memberId)).thenReturn(Optional.of(mong));
    }

    @Test
    void createMong_Success() {
        managementService.createMong(initMong, memberId);

        assertNotNull(mong);
        assertEquals(mong.getMemberId(),1L);
        assertEquals(mong.getName(), "MongName");
        assertEquals(mong.getSleepStart(), "22:00");
        assertEquals(mong.getSleepEnd(), "08:00");

        assertEquals(false, isSleep(mong.getSleepStart(), mong.getSleepEnd()));;

        assertNotNull(mong.getSleepStart());
        assertNotNull(mong.getSleepEnd());
        assertTrue(mong.getWeight() > 0);

        verify(managementRepository, times(1)).save(any(Management.class));
    }

    @Test
    void notFoundMember() {
        Long wrongMemberId = 0L;
        when(managementRepository.findManagementByMemberId(wrongMemberId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> managementService.toMongStroke(wrongMemberId))
                .isInstanceOf(ManagementException.class);
    }

    @Test
    void toMongStroke() {
        managementService.toMongStroke(memberId);
        assertEquals(mong.getStrokeCount(), 1);
    }

    @Test
    void toCleanMongsPoop() {
        managementService.toCleanMongsPoop(memberId);
        assertEquals(0, mong.getPoopCount());
    }

    @Test
    void mongTraining() {
        TrainingCount trainingCount = new TrainingCount(100);
        managementService.mongTraining(trainingCount, 1L);
        assertEquals(100, mong.getTrainingCount());
    }

    @Test
    void isMongSleep() {
        managementService.toCheckMongsLifetime(1L);

        Boolean sleep = isSleep(mong.getSleepStart(), mong.getSleepEnd());
        assertEquals(false, sleep);

        LocalTime startTime = LocalTime.parse(mong.getSleepStart(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(mong.getSleepEnd(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime currentTime = LocalTime.of(23, 0);

        if (endTime.isBefore(startTime) && !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime)) {
            sleep = true;
        }
        if(endTime.isBefore(startTime) && !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime)){
            sleep = false;
        }
        assertEquals(true, sleep);
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

    private String timeConverter(LocalDateTime time) {
        return time.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
