package com.mongs.management.domain.service;

import com.mongs.management.domain.entity.Management;
import com.mongs.management.domain.repository.ManagementRepository;
import com.mongs.management.domain.service.dto.InitMong;
import com.mongs.management.exception.ManagementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
        initMong = new InitMong("MongName", LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(8));
        mong = Management.builder()
                .memberId(memberId)
                .name(initMong.name())
                .sleepStart(initMong.sleepStart())
                .sleepEnd(initMong.sleepEnd())
                .weight(new Random().nextDouble() * 100)
                .sleep(true)
                .build();

        when(managementRepository.findManagementByMemberId(memberId)).thenReturn(Optional.of(mong));
    }

    @Test
    void createMong_Success() {
        managementService.createMong(initMong, memberId);

        assertNotNull(mong);
        assertEquals(mong.getMemberId(),1L);
        assertEquals(mong.getName(), "MongName");
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
}
