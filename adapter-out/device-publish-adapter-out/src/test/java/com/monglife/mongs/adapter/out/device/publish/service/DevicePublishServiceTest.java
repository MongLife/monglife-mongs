package com.monglife.mongs.adapter.out.device.publish.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.module.mqtt.config.MqttAutoConfig;
import com.monglife.mongs.adapter.out.device.publish.config.AdapterOutDevicePublishConfig;
import com.monglife.mongs.adapter.out.device.publish.dto.response.DevicePublishDto;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.domain.model.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@ContextConfiguration(classes = { AdapterOutDevicePublishConfig.class, MqttAutoConfig.class })
class DevicePublishServiceTest {

    private static final Logger log = LoggerFactory.getLogger(DevicePublishServiceTest.class);

    private final DevicePublishPort devicePublishPort;

    @Autowired
    public DevicePublishServiceTest(DevicePublishPort devicePublishPort) {
        this.devicePublishPort = devicePublishPort;
    }


    @Nested
    @DisplayName("보유 걸음 수 비동기 응답 단위 테스트")
    class publishCurrentWalkingCountPort {

        private static Queue<Map<String, DevicePublishDto>> consumeMessageQueue;

        protected static String deviceId;
        private static final LocalDateTime deviceBootedDt = LocalDateTime.of(2025, 1, 1, 0, 0);

        @BeforeEach
        void beforeEach() {
            deviceId = UUID.randomUUID().toString().replace("-", "");
            consumeMessageQueue = new ArrayDeque<>();
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

            // act
            devicePublishPort.publishCurrentWalkingCountPort(step);

            Thread.sleep(1000);

            int consumeMessageQueueSize = consumeMessageQueue.size();
            Map<String, DevicePublishDto> consumeMessageMap = consumeMessageQueue.poll();
            DevicePublishDto devicePublishDto = consumeMessageMap != null ? consumeMessageMap.get(deviceId) : null;

            // assert
            assertEquals(1, consumeMessageQueueSize);
            assertNotNull(devicePublishDto);
            assertEquals(deviceId, devicePublishDto.getDeviceId());
            assertEquals(walkingCount, devicePublishDto.getWalkingCount());
            assertEquals(consumeWalkingCount, devicePublishDto.getConsumeWalkingCount());
        }

        /**
         * Mqtt Consume Component
         * @hidden
         */
        @MqttConsumer
        private static class Consumer {

            private final ObjectMapper objectMapper;

            public Consumer(ObjectMapper objectMapper) {
                this.objectMapper = objectMapper;
                this.objectMapper.registerModule(new JavaTimeModule());
            }

            @MqttMapping("/device/{id}")
            public void consumeDevice(@PathVariable("id") String id, @MqttPayload ResponseDto<Map<String, Objects>> payload) {
                // mqtt library 는 제네릭 타입 Payload 를 변환할 수 없음 (직접 변환)
                DevicePublishDto devicePublishDto = objectMapper.convertValue(payload.getResult(), DevicePublishDto.class);
                if (devicePublishDto != null && id.equals(deviceId)) {
                    consumeMessageQueue.offer(Map.of(deviceId, devicePublishDto));
                }
            }
        }
    }
}