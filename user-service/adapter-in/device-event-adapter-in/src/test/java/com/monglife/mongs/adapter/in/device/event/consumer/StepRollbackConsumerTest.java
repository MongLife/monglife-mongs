package com.monglife.mongs.adapter.in.device.event.consumer;

import com.monglife.module.common.kafka.config.KafkaAutoConfig;
import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.transaction.ExchangeCurrentWalkingCountEventDto;
import com.monglife.mongs.application.device.port.in.StepUseCase;
import com.monglife.mongs.application.device.port.in.command.IncreaseCurrentWalkingCountCommand;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration(classes = { StepRollbackConsumer.class, KafkaAutoConfig.class })
@ComponentScan({"com.monglife.module.common.kafka"})
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext
class StepRollbackConsumerTest {

    @MockBean
    private StepUseCase stepUseCase;

    private final KafkaService kafkaService;

    @Autowired
    public StepRollbackConsumerTest(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @Nested
    @DisplayName("걸음 수 환전 트랜잭션 롤백 이벤트 리스너 단위 테스트")
    class ExchangeCurrentWalkingCountRollbackEvent {

        @Test
        @DisplayName("걸음 수 환전 트랜잭션이 실패하는 경우 롤백 이벤트를 소비하여 보유 걸음 수 증가 UseCase를 실행 한다.")
        void exchangeCurrentWalkingCountRollback() {
            // arrange
            String topic = "rollback.exchangeCurrentWalkingCount";
            String deviceId = "TEST-DEVICE-ID";
            long mongId = 1L;
            int walkingCount = 10;
            int payPoint = 100;

            // act
            ExchangeCurrentWalkingCountEventDto exchangeCurrentWalkingCountEventDto = ExchangeCurrentWalkingCountEventDto.builder()
                    .deviceId(deviceId)
                    .mongId(mongId)
                    .walkingCount(walkingCount)
                    .payPoint(payPoint)
                    .build();

            kafkaService.generateEvent(topic, exchangeCurrentWalkingCountEventDto);

            // assert
            ArgumentCaptor<IncreaseCurrentWalkingCountCommand> captor = ArgumentCaptor.forClass(IncreaseCurrentWalkingCountCommand.class);

            Awaitility.await()
                    .atMost(Duration.ofSeconds(30))
                    .untilAsserted(() -> Mockito.verify(stepUseCase, Mockito.times(1))
                                    .increaseCurrentWalkingCountUseCase(Mockito.any()));

            Mockito.verify(stepUseCase).increaseCurrentWalkingCountUseCase(captor.capture());

            var command = captor.getValue();
            assertEquals(deviceId, command.getDeviceId());
            assertEquals(walkingCount, command.getWalkingCount());
        }
    }
}