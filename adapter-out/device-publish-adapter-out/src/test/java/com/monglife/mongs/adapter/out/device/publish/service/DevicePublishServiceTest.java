package com.monglife.mongs.adapter.out.device.publish.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.mqtt.config.MqttAutoConfig;
import com.monglife.mongs.adapter.out.device.publish.config.AdapterOutDevicePublishConfig;
import com.monglife.mongs.adapter.out.device.publish.dto.response.DevicePublishDto;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.domain.model.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
@ContextConfiguration(classes = { AdapterOutDevicePublishConfig.class, MqttAutoConfig.class })
class DevicePublishServiceTest {

    private final DevicePublishPort devicePublishPort;

    private final Consumer consumer;

    @Autowired
    public DevicePublishServiceTest(DevicePublishPort devicePublishPort, Consumer consumer) {
        this.devicePublishPort = devicePublishPort;
        this.consumer = consumer;
    }


    @Nested
    @DisplayName("보유 걸음 수 비동기 응답 단위 테스트")
    class publishCurrentWalkingCountPort {

        protected static String deviceId;
        private static final LocalDateTime deviceBootedDt = LocalDateTime.of(2025, 1, 1, 0, 0);

        @BeforeEach
        void beforeEach() {
            deviceId = CommonUtil.randomId();
        }

        @Test
        @DisplayName("걸음 수 도메인 객체의 정보를 사용자의 기기로 비동기 전송 한다.")
        void publishCurrentWalkingCount() throws InterruptedException {
            // arrange
            int walkingCount = 50;
            int consumeWalkingCount = 100;
            Step step = Step.builder()
                    .deviceId(deviceId)
                    .walkingCount(walkingCount)
                    .totalWalkingCount(100)
                    .consumeWalkingCount(consumeWalkingCount)
                    .deviceBootedDt(deviceBootedDt)
                    .build();

            DevicePublishDto devicePublishDto = new DevicePublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);

            consumer.reset(deviceId, devicePublishDto, countDownLatch);

            // act
            devicePublishPort.publishCurrentWalkingCountPort(step);

            boolean messageConsumed = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(messageConsumed);
            assertEquals(deviceId, devicePublishDto.getDeviceId());
            assertEquals(walkingCount, devicePublishDto.getWalkingCount());
            assertEquals(consumeWalkingCount, devicePublishDto.getConsumeWalkingCount());
        }
    }
}