package com.monglife.mongs.adapter.out.member.event.service;

import com.monglife.module.common.kafka.config.KafkaAutoConfig;
import com.monglife.mongs.adapter.out.member.event.consumer.ExchangeStarPointConsumer;
import com.monglife.mongs.adapter.transaction.ExchangeStarPointEventDto;
import com.monglife.mongs.application.member.port.out.MemberEventPort;
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
@EmbeddedKafka(partitions = 1, bootstrapServersProperty = "module.kafka.url")
@DirtiesContext
class MemberEventServiceTest {

    private final MemberEventPort memberEventPort;

    @Autowired
    public MemberEventServiceTest(MemberEventPort memberEventPort) {
        this.memberEventPort = memberEventPort;
    }

    @Nested
    @DisplayName("스타 포인트 환전 분산 트랜잭션 이벤트 발생 단위 테스트")
    class ExchangeStarPointEventPort {

        @Autowired
        private ExchangeStarPointConsumer exchangeStarPointConsumer;

        @Test
        @DisplayName("환전할 스타 포인트, 환전할 페이 포인트 정보를 담아 스타 포인트 환전 이벤트를 발생 한다.")
        void exchangeStarPointEvent() throws InterruptedException {
            // arrange
            final long accountId = 1L;
            final long mongId = 1L;
            final int starPoint = 10;
            final int payPoint = 100;

            ExchangeStarPointEventDto exchangeStarPointEventDto = new ExchangeStarPointEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            exchangeStarPointConsumer.reset(exchangeStarPointEventDto, countDownLatch);

            // act
            memberEventPort.exchangeStarPointEventPort(accountId, mongId, starPoint, payPoint);

            var expected = countDownLatch.await(30, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(mongId, exchangeStarPointEventDto.getMongId());
            assertEquals(starPoint, exchangeStarPointEventDto.getStarPoint());
            assertEquals(payPoint, exchangeStarPointEventDto.getPayPoint());
        }
    }
}