package com.monglife.mongs.adapter.out.device.persistence.service;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.mongs.adapter.out.device.persistence.config.AdapterOutDevicePersistenceConfig;
import com.monglife.mongs.adapter.out.device.persistence.config.DeviceDataSourceConfig;
import com.monglife.mongs.adapter.out.device.persistence.entity.DeviceEntity;
import com.monglife.mongs.adapter.out.device.persistence.repository.DeviceRepository;
import com.monglife.mongs.application.device.port.out.DevicePersistencePort;
import com.monglife.mongs.application.device.port.out.vo.CreateStepVo;
import com.monglife.mongs.domain.device.model.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        AdapterOutDevicePersistenceConfig.class,
        DeviceDataSourceConfig.class,
        HibernateAutoConfig.class
})
class DevicePersistenceServiceTest {

    private final DevicePersistencePort devicePersistencePort;

    private final DeviceRepository deviceRepository;

    @Autowired
    public DevicePersistenceServiceTest(DevicePersistencePort devicePersistencePort, DeviceRepository deviceRepository) {
        this.devicePersistencePort = devicePersistencePort;
        this.deviceRepository = deviceRepository;
    }

    @Nested
    @DisplayName("걸음 수 등록 단위 테스트")
    class CreateStepPort {

        private static final String DEVICE_ID = "TEST-DEVICE-ID";
        private static final int TOTAL_WALKING_COUNT = 100;
        private static final LocalDateTime DEVICE_BOOTED_AT = LocalDateTime.of(2025, 1, 1, 0, 0);

        @Test
        @DisplayName("걸음 수를 등록하고 등록된 걸음 수 도메인 객체를 반환 한다.")
        void createStep() {
            // arrange
            final CreateStepVo createStepVo = CreateStepVo.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .consumeWalkingCount(0)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            // act
            Step step = devicePersistencePort.createStepPort(createStepVo);

            Optional<DeviceEntity> deviceEntityOptional = deviceRepository.findByDeviceId(DEVICE_ID);

            // assert
            assertNotNull(step);
            assertEquals(DEVICE_ID, step.getDeviceId());
            assertEquals(0, step.getWalkingCount());
            assertEquals(TOTAL_WALKING_COUNT, step.getTotalWalkingCount());
            assertEquals(0, step.getConsumeWalkingCount());
            assertEquals(DEVICE_BOOTED_AT, step.getDeviceBootedAt());

            assertTrue(deviceEntityOptional.isPresent());
            assertEquals(DEVICE_ID, deviceEntityOptional.get().getDeviceId());
            assertEquals(0, deviceEntityOptional.get().getWalkingCount());
            assertEquals(TOTAL_WALKING_COUNT, deviceEntityOptional.get().getTotalWalkingCount());
            assertEquals(0, deviceEntityOptional.get().getConsumeWalkingCount());
            assertEquals(DEVICE_BOOTED_AT, deviceEntityOptional.get().getDeviceBootedAt());
        }
    }

    @Nested
    @DisplayName("걸음 수 수정 단위 테스트")
    class SaveStepPort {

        private static final String DEVICE_ID = "TEST-DEVICE-ID";
        private static final int TOTAL_WALKING_COUNT = 100;
        private static final LocalDateTime DEVICE_BOOTED_AT = LocalDateTime.of(2025, 1, 1, 0, 0);

        @BeforeEach
        void beforeEach() {
            final DeviceEntity deviceEntity = DeviceEntity.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .totalWalkingCount(0)
                    .consumeWalkingCount(0)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            deviceRepository.saveAndFlush(deviceEntity);
        }

        @Test
        @DisplayName("걸음 수를 수정하고 수정된 걸음 수 옵셔널 도메인 객체를 반환 한다.")
        void saveStep() {
            // arrange
            final Step saveStep = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .consumeWalkingCount(0)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            // act
            Optional<Step> stepOptional = devicePersistencePort.saveStepPort(saveStep);

            // assert
            assertTrue(stepOptional.isPresent());
            assertEquals(DEVICE_ID, stepOptional.get().getDeviceId());
            assertEquals(0, stepOptional.get().getWalkingCount());
            assertEquals(TOTAL_WALKING_COUNT, stepOptional.get().getTotalWalkingCount());
            assertEquals(0, stepOptional.get().getConsumeWalkingCount());
            assertEquals(DEVICE_BOOTED_AT, stepOptional.get().getDeviceBootedAt());
        }

        @Test
        @DisplayName("걸음 수를 수정할 때 걸음 수가 존재하지 않는 경우 빈 옵셔널 객체를 반환 한다.")
        void notExistsStepWhenSaveStep() {
            // arrange
            final Step saveStep = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .consumeWalkingCount(0)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            deviceRepository.deleteById(DEVICE_ID);

            // act
            Optional<Step> stepOptional = devicePersistencePort.saveStepPort(saveStep);

            // assert
            assertTrue(stepOptional.isEmpty());
        }
    }

    @Nested
    @DisplayName("걸음 수 조회 단위 테스트")
    class GetStepPort {

        private static final String DEVICE_ID = "TEST-DEVICE-ID";
        private static final int TOTAL_WALKING_COUNT = 100;
        private static final LocalDateTime DEVICE_BOOTED_AT = LocalDateTime.of(2025, 1, 1, 0, 0);

        @BeforeEach
        void beforeEach() {
            final DeviceEntity deviceEntity = DeviceEntity.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(0)
                    .totalWalkingCount(TOTAL_WALKING_COUNT)
                    .consumeWalkingCount(0)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            deviceRepository.saveAndFlush(deviceEntity);
        }

        @Test
        @DisplayName("걸음 수를 조회하고 걸음 수 옵셔널 도메인 객체를 반환 한다.")
        void getStep() {
            // act
            Optional<Step> stepOptional = devicePersistencePort.getStepPort(DEVICE_ID);

            // assert
            assertTrue(stepOptional.isPresent());
            assertEquals(DEVICE_ID, stepOptional.get().getDeviceId());
            assertEquals(0, stepOptional.get().getWalkingCount());
            assertEquals(TOTAL_WALKING_COUNT, stepOptional.get().getTotalWalkingCount());
            assertEquals(0, stepOptional.get().getConsumeWalkingCount());
            assertEquals(DEVICE_BOOTED_AT, stepOptional.get().getDeviceBootedAt());
        }
    }
}