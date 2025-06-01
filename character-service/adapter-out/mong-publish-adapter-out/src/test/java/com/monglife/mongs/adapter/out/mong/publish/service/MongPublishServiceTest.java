package com.monglife.mongs.adapter.out.mong.publish.service;

import com.monglife.core.dto.event.SendNotificationDto;
import com.monglife.module.common.kafka.config.KafkaAutoConfig;
import com.monglife.module.mqtt.config.MqttAutoConfig;
import com.monglife.mongs.adapter.out.mong.publish.config.AdapterOutMongPublishConfig;
import com.monglife.mongs.adapter.out.mong.publish.consumer.MongConsumer;
import com.monglife.mongs.adapter.out.mong.publish.consumer.NotificationConsumer;
import com.monglife.mongs.adapter.out.mong.publish.dto.response.MongPublishDto;
import com.monglife.mongs.application.mong.port.out.MongPublishPort;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.model.Mong;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        AdapterOutMongPublishConfig.class,
        MqttAutoConfig.class,
        KafkaAutoConfig.class
})
@ComponentScan({ "com.monglife.module.common.kafka", "com.monglife.mongs.adapter" })
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext
class MongPublishServiceTest {

    private final MongPublishPort mongPublishPort;

    @Autowired
    public MongPublishServiceTest(MongPublishPort mongPublishPort) {
        this.mongPublishPort = mongPublishPort;
    }

    @Nested
    @DisplayName("몽 정보 비동기 응답 단위 테스트")
    class PublishMongPort {

        @Autowired
        private MongConsumer mongConsumer;

        @Test
        @DisplayName("몽 정보를 비동기 전송 한다.")
        void publishMong() throws InterruptedException {
            // arrange
            final long mongId = 1L;
            final long accountId = 1L;
            final double status = 50D;
            final double maxStatus = 100D;
            final Mong mong = Mong.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .mongCode("CH100")
                    .mongName("TEST-MONG-TYPE-NAME")
                    .name("TEST-MONG-NAME")
                    .stateCode(MongStateCode.NORMAL)
                    .statusCode(MongStatusCode.NORMAL)
                    .level(1)
                    .maxStatus(maxStatus)
                    .sleepAt(LocalTime.now())
                    .wakeupAt(LocalTime.now())
                    .payPoint(0)
                    .isSleep(false)
                    .strength(status)
                    .satiety(status)
                    .healthy(status)
                    .fatigue(status)
                    .exp(status)
                    .weight(status)
                    .evolutionReward(0D)
                    .evolutionPenalty(0D)
                    .strokeCount(0)
                    .trainingCount(0)
                    .poopCount(0)
                    .randomDrawTicketCount(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            MongPublishDto mongPublishDto = new MongPublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            mongConsumer.reset(mong.getMongId(), mongPublishDto, countDownLatch);

            // act
            mongPublishPort.publishMongPort(mong);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(mongId, mongPublishDto.getMongId());
            assertEquals(mong.getName(), mongPublishDto.getName());
            assertEquals(mong.getMongCode(), mongPublishDto.getMongCode());
            assertEquals(status / maxStatus * 100, mongPublishDto.getExpRatio());
            assertEquals(status / maxStatus * 100, mongPublishDto.getStrengthRatio());
            assertEquals(status / maxStatus * 100, mongPublishDto.getSatietyRatio());
            assertEquals(status / maxStatus * 100, mongPublishDto.getHealthyRatio());
            assertEquals(status / maxStatus * 100, mongPublishDto.getFatigueRatio());
        }
    }

    @Nested
    @DisplayName("몽 변동 알림 응답 단위 테스트")
    class PublishNotificationPort {

        @Autowired
        private NotificationConsumer notificationConsumer;

        @Test
        @DisplayName("몽 변동 알림을 전송 한다.")
        void publishNotification() throws InterruptedException {
            // arrange
            final long accountId = 1L;
            final String title = "TEST-TITLE";
            final String body = "TEST-BODY";

            SendNotificationDto sendNotificationDto = new SendNotificationDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            notificationConsumer.reset(sendNotificationDto, countDownLatch);

            // act
            mongPublishPort.publishNotificationPort(accountId, title, body);

            var expected = countDownLatch.await(30, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(accountId, sendNotificationDto.getAccountId());
            assertEquals(title, sendNotificationDto.getTitle());
            assertEquals(body, sendNotificationDto.getBody());
        }
    }
}