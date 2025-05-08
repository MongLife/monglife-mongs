package com.monglife.mongs.adapter.out.device.persistence.service;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.mongs.adapter.out.device.persistence.config.AdapterOutDevicePersistenceConfig;
import com.monglife.mongs.adapter.out.device.persistence.config.DeviceDataSourceConfig;
import com.monglife.mongs.adapter.out.device.persistence.entity.DeviceEntity;
import com.monglife.mongs.adapter.out.device.persistence.repository.DeviceRepository;
import com.monglife.mongs.application.device.port.out.DevicePersistencePort;
import com.monglife.mongs.application.device.port.out.vo.CreateStepVo;
import com.monglife.mongs.domain.model.Step;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = { AdapterOutDevicePersistenceConfig.class, DeviceDataSourceConfig.class, HibernateAutoConfig.class })
class DevicePersistenceServiceTest {

    private final DevicePersistencePort devicePersistencePort;

    private final DeviceRepository deviceRepository;

    @Autowired
    public DevicePersistenceServiceTest(DevicePersistencePort devicePersistencePort, DeviceRepository deviceRepository) {
        this.devicePersistencePort = devicePersistencePort;
        this.deviceRepository = deviceRepository;
    }

    private static final String deviceId = "TEST-DEVICE-ID";
    private static final int totalWalkingCount = 100;
    private static final LocalDateTime deviceBootedDt = LocalDateTime.of(2025, 1, 1, 0, 0);

    @Nested
    @DisplayName("걸음 수 등록 단위 테스트")
    class CreateStepPort {

        @Test
        @DisplayName("걸음 수를 등록하고 등록된 걸음 수 도메인 객체를 반환 한다.")
        void createStep() {
            // arrange
            CreateStepVo createStepVo = CreateStepVo.builder()
                    .deviceId(deviceId)
                    .walkingCount(0)
                    .totalWalkingCount(totalWalkingCount)
                    .consumeWalkingCount(0)
                    .deviceBootedDt(deviceBootedDt)
                    .build();

            // act
            Step step = devicePersistencePort.createStepPort(createStepVo);

            Optional<DeviceEntity> deviceEntityOptional = deviceRepository.findByDeviceId(deviceId);

            // assert
            assertNotNull(step);
            assertEquals(deviceId, step.getDeviceId());
            assertEquals(0, step.getWalkingCount());
            assertEquals(totalWalkingCount, step.getTotalWalkingCount());
            assertEquals(0, step.getConsumeWalkingCount());
            assertEquals(deviceBootedDt, step.getDeviceBootedDt());

            assertTrue(deviceEntityOptional.isPresent());
            assertEquals(deviceId, deviceEntityOptional.get().getDeviceId());
            assertEquals(0, deviceEntityOptional.get().getWalkingCount());
            assertEquals(totalWalkingCount, deviceEntityOptional.get().getTotalWalkingCount());
            assertEquals(0, deviceEntityOptional.get().getConsumeWalkingCount());
            assertEquals(deviceBootedDt, deviceEntityOptional.get().getDeviceBootedDt());
        }
    }

    @Nested
    @DisplayName("걸음 수 수정 단위 테스트")
    class SaveStepPort {

        @BeforeEach
        void beforeEach() {
            DeviceEntity deviceEntity = DeviceEntity.builder()
                    .deviceId(deviceId)
                    .walkingCount(0)
                    .totalWalkingCount(0)
                    .consumeWalkingCount(0)
                    .deviceBootedDt(deviceBootedDt)
                    .build();

            deviceRepository.saveAndFlush(deviceEntity);
        }

        @Test
        @DisplayName("걸음 수를 수정하고 수정된 걸음 수 옵셔널 도메인 객체를 반환 한다.")
        void saveStep() {
            // arrange
            Step saveStep = Step.builder()
                    .deviceId(deviceId)
                    .walkingCount(0)
                    .totalWalkingCount(totalWalkingCount)
                    .consumeWalkingCount(0)
                    .deviceBootedDt(deviceBootedDt)
                    .build();

            // act
            Optional<Step> stepOptional = devicePersistencePort.saveStepPort(saveStep);

            // assert
            assertTrue(stepOptional.isPresent());
            assertEquals(deviceId, stepOptional.get().getDeviceId());
            assertEquals(0, stepOptional.get().getWalkingCount());
            assertEquals(totalWalkingCount, stepOptional.get().getTotalWalkingCount());
            assertEquals(0, stepOptional.get().getConsumeWalkingCount());
            assertEquals(deviceBootedDt, stepOptional.get().getDeviceBootedDt());
        }

        @Test
        @DisplayName("걸음 수를 수정할 때 걸음 수가 존재하지 않는 경우 빈 옵셔널 객체를 반환 한다.")
        void notExistsStepWhenSaveStep() {
            // arrange
            Step saveStep = Step.builder()
                    .deviceId(deviceId)
                    .walkingCount(0)
                    .totalWalkingCount(totalWalkingCount)
                    .consumeWalkingCount(0)
                    .deviceBootedDt(deviceBootedDt)
                    .build();

            deviceRepository.deleteById(deviceId);

            // act
            Optional<Step> stepOptional = devicePersistencePort.saveStepPort(saveStep);

            // assert
            assertTrue(stepOptional.isEmpty());
        }
    }

    @Nested
    @DisplayName("걸음 수 조회 단위 테스트")
    class GetStepPort {

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
        @DisplayName("걸음 수를 조회하고 걸음 수 옵셔널 도메인 객체를 반환 한다.")
        void getStep() {
            // act
            Optional<Step> stepOptional = devicePersistencePort.getStepPort(deviceId);

            // assert
            assertTrue(stepOptional.isPresent());
            assertEquals(deviceId, stepOptional.get().getDeviceId());
            assertEquals(0, stepOptional.get().getWalkingCount());
            assertEquals(totalWalkingCount, stepOptional.get().getTotalWalkingCount());
            assertEquals(0, stepOptional.get().getConsumeWalkingCount());
            assertEquals(deviceBootedDt, stepOptional.get().getDeviceBootedDt());
        }
    }
}