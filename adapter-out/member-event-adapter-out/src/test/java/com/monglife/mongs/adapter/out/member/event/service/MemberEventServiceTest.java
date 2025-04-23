package com.monglife.mongs.adapter.out.member.event.service;

import com.monglife.module.common.kafka.config.KafkaAutoConfig;
import com.monglife.mongs.adapter.out.member.event.config.AdapterOutMemberEventConfig;
import com.monglife.mongs.adapter.transaction.commit.ExchangeStarPointEventDto;
import com.monglife.mongs.application.member.port.out.MemberEventPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
@ContextConfiguration(classes = { AdapterOutMemberEventConfig.class, KafkaAutoConfig.class })
class MemberEventServiceTest {

    private final MemberEventPort memberEventPort;

    private final Consumer consumer;

    @Autowired
    public MemberEventServiceTest(MemberEventPort memberEventPort, Consumer consumer) {
        this.memberEventPort = memberEventPort;
        this.consumer = consumer;
    }

    @Nested
    @DisplayName("스타 포인트 환전 분산 트랜잭션 이벤트 발생 단위 테스트")
    class ExchangeStarPointEventPort {

        @Test
        @DisplayName("환전할 스타 포인트, 환전할 페이 포인트 정보를 담아 스타 포인트 환전 이벤트를 발생 한다.")
        void exchangeStarPointEvent() throws InterruptedException {
            // arrange
            long accountId = 1L;
            long mongId = 1L;
            int starPoint = 10;
            int payPoint = 100;

            ExchangeStarPointEventDto exchangeStarPointEventDto = new ExchangeStarPointEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);

            consumer.reset(exchangeStarPointEventDto, countDownLatch);

            // act
            memberEventPort.exchangeStarPointEventPort(accountId, mongId, starPoint, payPoint);

            boolean messageConsumed = countDownLatch.await(30, TimeUnit.SECONDS);

            // assert
            assertTrue(messageConsumed);
            assertEquals(mongId, exchangeStarPointEventDto.getMongId());
            assertEquals(starPoint, exchangeStarPointEventDto.getStarPoint());
            assertEquals(payPoint, exchangeStarPointEventDto.getPayPoint());
        }
    }
}