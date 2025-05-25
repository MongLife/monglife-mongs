package com.monglife.mongs.adapter.out.battle.publish.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.mqtt.config.MqttAutoConfig;
import com.monglife.mongs.adapter.out.battle.publish.config.AdapterOutBattlePublishConfig;
import com.monglife.mongs.adapter.out.battle.publish.consumer.MatchingQueuePlayerConsumer;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchingQueuePlayerFailPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchingQueuePlayerPublishDto;
import com.monglife.mongs.application.battle.port.out.QueuePublishPort;
import com.monglife.mongs.domain.battle.enums.MatchRoundCode;
import com.monglife.mongs.domain.battle.enums.MatchStateCode;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import com.monglife.mongs.domain.battle.model.QueuePlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        AdapterOutBattlePublishConfig.class,
        MqttAutoConfig.class,
})
class QueuePublishServiceTest {

    private final QueuePublishPort queuePublishPort;

    @Autowired
    public QueuePublishServiceTest(QueuePublishPort queuePublishPort) {
        this.queuePublishPort = queuePublishPort;
    }

    @Nested
    @DisplayName("매칭 비동기 응답 단위 테스트")
    class PublishMatchingQueuePlayerPort {

        private final MatchingQueuePlayerConsumer matchingQueuePlayerConsumer;

        @Autowired
        public PublishMatchingQueuePlayerPort(MatchingQueuePlayerConsumer matchingQueuePlayerConsumer) {
            this.matchingQueuePlayerConsumer = matchingQueuePlayerConsumer;
        }

        @Test
        @DisplayName("매칭 성공 정보를 비동기 전송 한다.")
        void publishMatchingQueuePlayer() throws InterruptedException {
            // arrange
            List<MatchPlayer> matchPlayers = List.of(
                    MatchPlayer.builder()
                            .playerId(CommonUtil.randomId())
                            .deviceId(CommonUtil.randomId())
                            .accountId(1L)
                            .mongId(1L)
                            .mongTypeCode("MONG-TYPE-CODE")
                            .mongTypeName("MONG-TYPE-NAME")
                            .mongName("MONG-NAME")
                            .attack(0D)
                            .heal(0D)
                            .defence(0D)
                            .isBot(false)
                            .hp(100D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .exitedAt(null)
                            .damage(0D)
                            .recovery(0D)
                            .matchRoundCode(MatchRoundCode.NONE)
                            .build(),
                    MatchPlayer.builder()
                            .playerId(CommonUtil.randomId())
                            .deviceId(CommonUtil.randomId())
                            .accountId(2L)
                            .mongId(2L)
                            .mongTypeCode("MONG-TYPE-CODE 2")
                            .mongTypeName("MONG-TYPE-NAME 2")
                            .mongName("MONG-NAME 2")
                            .attack(0D)
                            .heal(0D)
                            .defence(0D)
                            .isBot(false)
                            .hp(100D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .exitedAt(null)
                            .damage(0D)
                            .recovery(0D)
                            .matchRoundCode(MatchRoundCode.NONE)
                            .build());

            Match match = Match.builder()
                    .matchId(1L)
                    .maxRound(10)
                    .matchPlayers(matchPlayers)
                    .round(0)
                    .matchStateCode(MatchStateCode.PROCESS)
                    .build();

            List<String> deviceIds = new ArrayList<>();
            MatchingQueuePlayerPublishDto matchingQueuePlayerPublishDto = new MatchingQueuePlayerPublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(matchPlayers.size());
            matchingQueuePlayerConsumer.reset(deviceIds, matchingQueuePlayerPublishDto, countDownLatch);

            // act
            queuePublishPort.publishMatchingQueuePlayerPort(match);
            var expected1 = countDownLatch.await(5 * matchPlayers.size(), TimeUnit.SECONDS);
            var expected2 = matchPlayers.stream().map(MatchPlayer::getDeviceId).toList();

            // assert
            assertTrue(expected1);
            assertTrue(expected2.containsAll(deviceIds));
            assertEquals(matchingQueuePlayerPublishDto.getMatchId(), match.getMatchId());
            matchingQueuePlayerPublishDto.getMatchPlayers().forEach(matchPlayer -> {
                assertTrue(expected2.contains(matchPlayer.getDeviceId()));
            });
        }

        @Test
        @DisplayName("매칭 대기열 등록 실패 정보를 비동기 전송 한다.")
        void publishMatchingQueuePlayerFail() throws InterruptedException {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            String deviceId = CommonUtil.randomId();
            QueuePlayer queuePlayer = QueuePlayer.builder()
                    .mongId(mongId)
                    .deviceId(deviceId)
                    .accountId(accountId)
                    .build();

            MatchingQueuePlayerFailPublishDto matchingQueuePlayerFailPublishDto = new MatchingQueuePlayerFailPublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            matchingQueuePlayerConsumer.reset(deviceId, matchingQueuePlayerFailPublishDto, countDownLatch);

            // act
            queuePublishPort.publishMatchingQueuePlayerFailPort(queuePlayer);
            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(matchingQueuePlayerFailPublishDto.getDeviceId(), deviceId);
        }
    }
}