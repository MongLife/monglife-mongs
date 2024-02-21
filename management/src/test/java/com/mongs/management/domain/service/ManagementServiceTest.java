package com.mongs.management.domain.service;

import com.mongs.management.domain.entity.Management;
import com.mongs.management.domain.repository.ManagementRepository;
import com.mongs.management.domain.service.dto.CreateMong;
import com.mongs.management.domain.service.dto.InitMong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Random;

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

    @BeforeEach
    void setUp() {
        memberId = 1L;
        initMong = new InitMong("MongName", LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(8));
    }

    @Test
    void createMong_Success() {
        Management expectedManagement = Management.builder()
                .memberId(memberId)
                .name(initMong.name())
                .sleepStart(initMong.sleepStart())
                .sleepEnd(initMong.sleepEnd())
                .weight(new Random().nextDouble() * 100)
                .sleep(true)
                .build();

        managementService.createMong(initMong, memberId);

        assertNotNull(expectedManagement);
        assertEquals(expectedManagement.getMemberId(),1L);
        assertEquals(expectedManagement.getName(), "MongName");
        assertNotNull(expectedManagement.getSleepStart());
        assertNotNull(expectedManagement.getSleepEnd());
        assertTrue(expectedManagement.getWeight() > 0);

        verify(managementRepository, times(1)).save(any(Management.class));
    }
}
