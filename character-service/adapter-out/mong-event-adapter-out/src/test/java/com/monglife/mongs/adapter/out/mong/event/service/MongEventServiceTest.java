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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = { KafkaAutoConfig.class })
@ComponentScan({ "com.monglife.module.common.kafka", "com.monglife.mongs.adapter" })
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
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

        private final CreateMongConsumer createMongConsumer;

        @Autowired
        public CreateMongEventPort(CreateMongConsumer createMongConsumer) {
            this.createMongConsumer = createMongConsumer;
        }

        @Test
        @DisplayName("몽 생성 이벤트를 발생 시킨다.")
        void createMongEvent() throws InterruptedException {
            // arrange
            long accountId = 1L;
            String mongTypeCode = "MONG_TYPE_CODE";

            CreateMongEventDto createMongEventDto = new CreateMongEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            createMongConsumer.reset(createMongEventDto, countDownLatch);

            // act
            mongEventPort.createMongEventPort(accountId, mongTypeCode);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(accountId, createMongEventDto.getAccountId());
            assertEquals(mongTypeCode, createMongEventDto.getMongTypeCode());
        }
    }

    @Nested
    @DisplayName("몽 진화 이벤트 발생 단위 테스트")
    class EvolutionMongEventPort{

        private final EvolutionMongConsumer evolutionMongConsumer;

        @Autowired
        public EvolutionMongEventPort(EvolutionMongConsumer evolutionMongConsumer) {
            this.evolutionMongConsumer = evolutionMongConsumer;
        }

        @Test
        @DisplayName("몽 진화 이벤트를 발생 시킨다.")
        void evolutionMongEvent() throws InterruptedException {
            // arrange
            long accountId = 1L;
            String mongTypeCode = "MONG_TYPE_CODE";

            EvolutionMongEventDto evolutionMongEventDto = new EvolutionMongEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            evolutionMongConsumer.reset(evolutionMongEventDto, countDownLatch);

            // act
            mongEventPort.evolutionMongEventPort(accountId, mongTypeCode);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(accountId, evolutionMongEventDto.getAccountId());
            assertEquals(mongTypeCode, evolutionMongEventDto.getMongTypeCode());
        }
    }

    @Nested
    @DisplayName("컬렉션 맵 랜덤 뽑기 이벤트 발생 단위 테스트")
    class RandomDrawMapEventPort{

        private final RandomDrawMapConsumer randomDrawMapConsumer;

        @Autowired
        public RandomDrawMapEventPort(RandomDrawMapConsumer randomDrawMapConsumer) {
            this.randomDrawMapConsumer = randomDrawMapConsumer;
        }

        @Test
        @DisplayName("컬렉션 몽 랜덤 뽑기 이벤트를 발생 시킨다.")
        void randomDrawMapEvent() throws InterruptedException {
            // arrange
            long accountId = 1L;
            String mapTypeCode = "MAP_TYPE_CODE";

            RandomDrawMapEventDto randomDrawMapEventDto = new RandomDrawMapEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            randomDrawMapConsumer.reset(randomDrawMapEventDto, countDownLatch);

            // act
            mongEventPort.randomDrawMapEventPort(accountId, mapTypeCode);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(accountId, randomDrawMapEventDto.getAccountId());
            assertEquals(mapTypeCode, randomDrawMapEventDto.getMapTypeCode());
        }
    }
}