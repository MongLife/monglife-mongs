package com.monglife.mongs.adapter.in.member.event.consumer;

import com.monglife.module.common.kafka.config.KafkaAutoConfig;
import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.transaction.ExchangeStarPointRollbackEventDto;
import com.monglife.mongs.application.member.port.in.PlayerUseCase;
import com.monglife.mongs.application.member.port.in.command.IncreaseStarPointCommand;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = { PlayerRollbackConsumer.class, KafkaAutoConfig.class })
@ComponentScan({"com.monglife.module.common.kafka"})
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext
class PlayerRollbackConsumerTest {

    @MockBean
    private PlayerUseCase playerUseCase;

    private final KafkaService kafkaService;

    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @Autowired
    public PlayerRollbackConsumerTest(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @Nested
    @DisplayName("스타 포인트 환전 트랜잭션 롤백 이벤트 리스너 단위 테스트")
    class ExchangeStarPointRollbackEvent {

        @Test
        @DisplayName("스타 포인트 환전 트랜잭션이 실패하는 경우 롤백 이벤트를 소비하여 스타 포인트 증가 UseCase를 실행 한다.")
        void exchangeStarPointRollback() {
            // arrange
            final String topic = profile + "." + EventTopic.ROLLBACK_EXCHANGE_STAR_POINT;
            final long accountId = 1L;
            final long mongId = 1L;
            final int starPoint = 10;
            final int payPoint = 100;

            // act
            ExchangeStarPointRollbackEventDto exchangeStarPointRollbackEventDto = ExchangeStarPointRollbackEventDto.builder()
                    .accountId(accountId)
                    .mongId(mongId)
                    .starPoint(starPoint)
                    .payPoint(payPoint)
                    .build();

            kafkaService.generateEventWithProfile(topic, exchangeStarPointRollbackEventDto);

            // assert
            ArgumentCaptor<IncreaseStarPointCommand> captor = ArgumentCaptor.forClass(IncreaseStarPointCommand.class);

            Awaitility.await()
                    .atMost(Duration.ofSeconds(30))
                    .untilAsserted(() -> Mockito.verify(playerUseCase, Mockito.times(1))
                            .increaseStarPointUseCase(Mockito.any()));

            Mockito.verify(playerUseCase).increaseStarPointUseCase(captor.capture());

            var command = captor.getValue();
            assertEquals(accountId, command.getAccountId());
            assertEquals(starPoint, command.getStarPoint());
        }
    }
}