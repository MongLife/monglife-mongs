package com.monglife.mongs.adapter.out.device.publish.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.mqtt.config.MqttAutoConfig;
import com.monglife.mongs.adapter.out.device.publish.config.AdapterOutDevicePublishConfig;
import com.monglife.mongs.adapter.out.device.publish.consumer.DeviceConsumer;
import com.monglife.mongs.adapter.out.device.publish.dto.response.DevicePublishDto;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.domain.device.model.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        AdapterOutDevicePublishConfig.class,
        MqttAutoConfig.class
})
class DevicePublishServiceTest {

    private final DevicePublishPort devicePublishPort;

    @Autowired
    public DevicePublishServiceTest(DevicePublishPort devicePublishPort) {
        this.devicePublishPort = devicePublishPort;
    }


    @Nested
    @DisplayName("보유 걸음 수 비동기 응답 단위 테스트")
    class PublishCurrentWalkingCountPort {

        private final DeviceConsumer deviceConsumer;

        @Autowired
        public PublishCurrentWalkingCountPort(DeviceConsumer deviceConsumer) {
            this.deviceConsumer = deviceConsumer;
        }

        private static String DEVICE_ID;
        private static final LocalDateTime DEVICE_BOOTED_AT = LocalDateTime.of(2025, 1, 1, 0, 0);

        @BeforeEach
        void beforeEach() {
            DEVICE_ID = CommonUtil.randomId();
        }

        @Test
        @DisplayName("걸음 수 도메인 객체의 정보를 사용자의 기기로 비동기 전송 한다.")
        void publishCurrentWalkingCount() throws InterruptedException {
            // arrange
            int walkingCount = 50;
            int consumeWalkingCount = 100;
            Step step = Step.builder()
                    .deviceId(DEVICE_ID)
                    .walkingCount(walkingCount)
                    .totalWalkingCount(100)
                    .consumeWalkingCount(consumeWalkingCount)
                    .deviceBootedAt(DEVICE_BOOTED_AT)
                    .build();

            DevicePublishDto devicePublishDto = new DevicePublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);

            deviceConsumer.reset(DEVICE_ID, devicePublishDto, countDownLatch);

            // act
            devicePublishPort.publishCurrentWalkingCountPort(step);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(DEVICE_ID, devicePublishDto.getDeviceId());
            assertEquals(walkingCount, devicePublishDto.getWalkingCount());
            assertEquals(consumeWalkingCount, devicePublishDto.getConsumeWalkingCount());
        }
    }
}