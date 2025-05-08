package com.monglife.mongs.adapter.out.device.persistence.repository;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.mongs.adapter.out.device.persistence.config.DeviceDataSourceConfig;
import com.monglife.mongs.adapter.out.device.persistence.entity.DeviceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
@ContextConfiguration(classes = { DeviceDataSourceConfig.class, HibernateAutoConfig.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeviceRepositoryTest {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceRepositoryTest(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    private static final String deviceId = "TEST-DEVICE-ID";
    private static final int totalWalkingCount = 100;
    private static final LocalDateTime deviceBootedDt = LocalDateTime.of(2025, 1, 1, 0, 0);

    @Nested
    @DisplayName("기기 엔티티 조회 단위 테스트")
    class Find {

        @BeforeEach
        void beforeEach() {
            DeviceEntity deviceEntity = DeviceEntity.builder()
                    .deviceId(deviceId)
                    .walkingCount(0)
                    .totalWalkingCount(totalWalkingCount)
                    .consumeWalkingCount(0)
                    .deviceBootedDt(deviceBootedDt)
                    .build();

            deviceRepository.saveAndFlush(deviceEntity);
        }

        @Test
        @DisplayName("기기 ID 기준으로 기기 엔티티를 조회 한다.")
        void findByDeviceId() {
            // act
            Optional<DeviceEntity> deviceEntityOptional = deviceRepository.findByDeviceId(deviceId);

            // assert
            assertTrue(deviceEntityOptional.isPresent());
            assertEquals(deviceId, deviceEntityOptional.get().getDeviceId());
            assertEquals(totalWalkingCount, deviceEntityOptional.get().getTotalWalkingCount());
            assertEquals(deviceBootedDt, deviceEntityOptional.get().getDeviceBootedDt());
        }
    }
}