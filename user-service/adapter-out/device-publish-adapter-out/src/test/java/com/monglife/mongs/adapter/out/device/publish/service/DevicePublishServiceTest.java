package com.monglife.mongs.adapter.out.device.publish.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.mqtt.config.MqttAutoConfig;
import com.monglife.mongs.adapter.out.device.publish.config.AdapterOutDevicePublishConfig;
import com.monglife.mongs.adapter.out.device.publish.consumer.DeviceConsumer;
import com.monglife.mongs.adapter.out.device.publish.dto.response.DevicePublishDto;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.application.device.port.out.dto.RestoreWalkingCountDto;
import com.monglife.mongs.adapter.out.device.publish.utils.MqttTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

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
class DevicePublishServiceTest extends MqttTestContainer {

    private final DevicePublishPort devicePublishPort;

    @Autowired
    public DevicePublishServiceTest(DevicePublishPort devicePublishPort) {
        this.devicePublishPort = devicePublishPort;
    }

    @Nested
    @DisplayName("걸음 수 복구 알림 비동기 발행 단위 테스트")
    class PublishRestoreWalkingCountPort {

        @Autowired
        private DeviceConsumer deviceConsumer;

        private static String DEVICE_ID;

        @BeforeEach
        void beforeEach() {
            DEVICE_ID = CommonUtil.randomId();
        }

        @Test
        @DisplayName("되돌릴 걸음 수와 중복 방지 키를 사용자의 기기로 비동기 전송 한다.")
        void publishRestoreWalkingCount() throws InterruptedException {
            // arrange
            final int restoreWalkingCount = 1_000;
            final String eventId = "TEST-TRANSACTION-ID";

            DevicePublishDto devicePublishDto = new DevicePublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            deviceConsumer.reset(DEVICE_ID, devicePublishDto, countDownLatch);

            // act
            devicePublishPort.publishRestoreWalkingCountPort(RestoreWalkingCountDto.builder()
                    .deviceId(DEVICE_ID)
                    .restoreWalkingCount(restoreWalkingCount)
                    .eventId(eventId)
                    .build());

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(DEVICE_ID, devicePublishDto.getDeviceId());
            assertEquals(restoreWalkingCount, devicePublishDto.getRestoreWalkingCount());
            assertEquals(eventId, devicePublishDto.getEventId());

            // 구 토픽으로는 아무것도 나가면 안 된다.
            // 이미 배포된 앱들이 그 토픽을 잔액 스냅샷으로 해석해 로컬 잔액을 덮어쓴다.
            assertEquals(0, deviceConsumer.getLegacyTopicReceiveCount());
        }
    }
}
