package com.monglife.mongs.adapter.out.device.event.service;

import com.monglife.module.common.kafka.config.KafkaAutoConfig;
import com.monglife.mongs.adapter.out.device.event.consumer.ExchangeCurrentWalkingCountEventConsumer;
import com.monglife.mongs.adapter.transaction.ExchangeCurrentWalkingCountEventDto;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import com.monglife.mongs.application.device.port.out.dto.ExchangeCurrentWalkingCountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = { KafkaAutoConfig.class })
@ComponentScan({ "com.monglife.module.common.kafka", "com.monglife.mongs.adapter" })
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext
class DeviceEventServiceTest {

    private final DeviceEventPort deviceEventPort;

    @Autowired
    public DeviceEventServiceTest(DeviceEventPort deviceEventPort) {
        this.deviceEventPort = deviceEventPort;
    }

    @Nested
    @DisplayName("보유 걸음 수 환전 분산 트랜잭션 이벤트 발생 단위 테스트")
    class ExchangeCurrentWalkingCountEventPort {

        @Autowired
        private ExchangeCurrentWalkingCountEventConsumer exchangeCurrentWalkingCountEventConsumer;

        @Test
        @DisplayName("환전할 걸음 수, 환전할 페이 포인트 정보를 담아 걸음 수 환전 이벤트를 발생 한다.")
        void exchangeCurrentWalkingCountEvent() throws InterruptedException {
            // arrange
            final String deviceId = "TEST-DEVICE-ID";
            final long mongId = 1L;
            final int walkingCount = 10;
            final int payPoint = 100;

            ExchangeCurrentWalkingCountEventDto exchangeCurrentWalkingCountEventDto = new ExchangeCurrentWalkingCountEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            exchangeCurrentWalkingCountEventConsumer.reset(exchangeCurrentWalkingCountEventDto, countDownLatch);

            // act
            deviceEventPort.exchangeCurrentWalkingCountEventPort(ExchangeCurrentWalkingCountDto.builder()
                    .deviceId(deviceId)
                    .mongId(mongId)
                    .walkingCount(walkingCount)
                    .payPoint(payPoint)
                    .build());

            var expected = countDownLatch.await(30, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(deviceId, exchangeCurrentWalkingCountEventDto.getDeviceId());
            assertEquals(mongId, exchangeCurrentWalkingCountEventDto.getMongId());
            assertEquals(walkingCount, exchangeCurrentWalkingCountEventDto.getWalkingCount());
            assertEquals(payPoint, exchangeCurrentWalkingCountEventDto.getPayPoint());
        }
    }
}