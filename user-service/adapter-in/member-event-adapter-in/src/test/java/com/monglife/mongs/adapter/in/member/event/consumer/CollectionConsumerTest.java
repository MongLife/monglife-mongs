package com.monglife.mongs.adapter.in.member.event.consumer;

import com.monglife.module.common.kafka.config.KafkaAutoConfig;
import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.transaction.CreateMongEventDto;
import com.monglife.mongs.application.member.port.in.CollectionUseCase;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMongCommand;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
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
@ContextConfiguration(classes = { CollectionConsumer.class, KafkaAutoConfig.class })
@ComponentScan({"com.monglife.module.common.kafka"})
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext
class CollectionConsumerTest {

    @MockBean
    private CollectionUseCase collectionUseCase;

    private final KafkaService kafkaService;

    @Autowired
    public CollectionConsumerTest(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @Nested
    @DisplayName("몽 등록 트랜잭션 이벤트 리스너 단위 테스트")
    class CreateMongEvent {

        @Test
        @DisplayName("몽 등록 트랜잭션이 성공하는 경우 이벤트를 소비하여 컬렉션 몽 등록 UseCase를 실행 한다.")
        void createMong() {
            // arrange
            final String topic = EventTopic.COMMIT_CREATE_MONG;
            final long accountId = 1L;
            final String mongCode = "CH000";

            // act
            CreateMongEventDto createMongEventDto = CreateMongEventDto.builder()
                    .accountId(accountId)
                    .mongCode(mongCode)
                    .build();

            kafkaService.generateEvent(topic, createMongEventDto);

            // assert
            ArgumentCaptor<CreateCollectionMongCommand> captor = ArgumentCaptor.forClass(CreateCollectionMongCommand.class);

            Awaitility.await()
                    .atMost(Duration.ofSeconds(30))
                    .untilAsserted(() -> Mockito.verify(collectionUseCase, Mockito.times(1))
                            .createCollectionMongUseCase(Mockito.any()));

            Mockito.verify(collectionUseCase).createCollectionMongUseCase(captor.capture());

            var command = captor.getValue();
            assertEquals(accountId, command.getAccountId());
            assertEquals(mongCode, command.getMongCode());
        }
    }
}