package com.monglife.mongs.adapter.out.mong.event.service;

import com.monglife.module.common.kafka.config.KafkaAutoConfig;
import com.monglife.mongs.adapter.out.mong.event.consumer.CreateMongConsumer;
import com.monglife.mongs.adapter.out.mong.event.consumer.EvolutionMongConsumer;
import com.monglife.mongs.adapter.out.mong.event.consumer.RandomDrawMapConsumer;
import com.monglife.mongs.adapter.transaction.CreateMongEventDto;
import com.monglife.mongs.adapter.transaction.EvolutionMongEventDto;
import com.monglife.mongs.adapter.transaction.RandomDrawMapEventDto;
import com.monglife.mongs.application.mong.port.out.MongEventPort;
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
class MongEventServiceTest {

    private final MongEventPort mongEventPort;

    @Autowired
    public MongEventServiceTest(MongEventPort mongEventPort) {
        this.mongEventPort = mongEventPort;
    }

    @Nested
    @DisplayName("몽 생성 이벤트 발생 단위 테스트")
    class CreateMongEventPort{

        @Autowired
        private CreateMongConsumer createMongConsumer;

        @Test
        @DisplayName("몽 생성 이벤트를 발생 시킨다.")
        void createMongEvent() throws InterruptedException {
            // arrange
            final long accountId = 1L;
            final String mongCode = "MONG_TYPE_CODE";

            CreateMongEventDto createMongEventDto = new CreateMongEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            createMongConsumer.reset(createMongEventDto, countDownLatch);

            // act
            mongEventPort.createMongEventPort(accountId, mongCode);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(accountId, createMongEventDto.getAccountId());
            assertEquals(mongCode, createMongEventDto.getMongCode());
        }
    }

    @Nested
    @DisplayName("몽 진화 이벤트 발생 단위 테스트")
    class EvolutionMongEventPort{

        @Autowired
        private EvolutionMongConsumer evolutionMongConsumer;

        @Test
        @DisplayName("몽 진화 이벤트를 발생 시킨다.")
        void evolutionMongEvent() throws InterruptedException {
            // arrange
            final long accountId = 1L;
            final String mongCode = "MONG_TYPE_CODE";

            EvolutionMongEventDto evolutionMongEventDto = new EvolutionMongEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            evolutionMongConsumer.reset(evolutionMongEventDto, countDownLatch);

            // act
            mongEventPort.evolutionMongEventPort(accountId, mongCode);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(accountId, evolutionMongEventDto.getAccountId());
            assertEquals(mongCode, evolutionMongEventDto.getMongCode());
        }
    }

    @Nested
    @DisplayName("컬렉션 맵 랜덤 뽑기 이벤트 발생 단위 테스트")
    class RandomDrawMapEventPort{

        @Autowired
        private RandomDrawMapConsumer randomDrawMapConsumer;

        @Test
        @DisplayName("컬렉션 몽 랜덤 뽑기 이벤트를 발생 시킨다.")
        void randomDrawMapEvent() throws InterruptedException {
            // arrange
            final long accountId = 1L;
            final String mapCode = "MAP_TYPE_CODE";

            RandomDrawMapEventDto randomDrawMapEventDto = new RandomDrawMapEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            randomDrawMapConsumer.reset(randomDrawMapEventDto, countDownLatch);

            // act
            mongEventPort.randomDrawMapEventPort(accountId, mapCode);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(accountId, randomDrawMapEventDto.getAccountId());
            assertEquals(mapCode, randomDrawMapEventDto.getMapCode());
        }
    }
}