package com.monglife.mongs.adapter.out.battle.publish.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.mqtt.config.MqttAutoConfig;
import com.monglife.mongs.adapter.out.battle.publish.config.AdapterOutBattlePublishConfig;
import com.monglife.mongs.adapter.out.battle.publish.consumer.MatchConsumer;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchPublishDto;
import com.monglife.mongs.application.battle.port.out.MatchPublishPort;
import com.monglife.mongs.domain.battle.enums.MatchStateCode;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import com.monglife.mongs.adapter.out.battle.publish.utils.MqttTestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        AdapterOutBattlePublishConfig.class,
        MqttAutoConfig.class,
})
class MatchPublishServiceTest extends MqttTestContainer {

    private final MatchPublishPort matchPublishPort;

    @Autowired
    public MatchPublishServiceTest(MatchPublishPort matchPublishPort) {
        this.matchPublishPort = matchPublishPort;
    }

    @Nested
    @DisplayName("매치 비동기 응답 단위 테스트")
    class PublishMatchPort {

        @Autowired
        private MatchConsumer matchConsumer;

        @Test
        @DisplayName("매치 정보를 비동기 전송 한다.")
        void publishMatch() throws InterruptedException {
            // arrange
            final long matchId = 1L;
            final List<MatchPlayer> matchPlayers = List.of(
                    MatchPlayer.builder()
                            .playerId(CommonUtil.randomId())
                            .deviceId(CommonUtil.randomId())
                            .accountId(1L)
                            .mongId(1L)
                            .mongCode("MONG-TYPE-CODE")
                            .mongName("MONG-TYPE-NAME")
                            .name("MONG-NAME")
                            .attack(0D)
                            .heal(0D)
                            .defence(0D)
                            .isBot(false)
                            .hp(100D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .exitedAt(null)
                            .build(),
                    MatchPlayer.builder()
                            .playerId(CommonUtil.randomId())
                            .deviceId(CommonUtil.randomId())
                            .accountId(2L)
                            .mongId(2L)
                            .mongCode("MONG-TYPE-CODE 2")
                            .mongName("MONG-TYPE-NAME 2")
                            .name("MONG-NAME 2")
                            .attack(0D)
                            .heal(0D)
                            .defence(0D)
                            .isBot(false)
                            .hp(100D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .exitedAt(null)
                            .build());

            final Match match = Match.builder()
                    .matchId(matchId)
                    .maxRound(10)
                    .matchPlayers(matchPlayers)
                    .round(0)
                    .stateCode(MatchStateCode.PROCESS)
                    .build();

            MatchPublishDto matchPublishDto = new MatchPublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            matchConsumer.reset(matchId, matchPublishDto, countDownLatch);

            // act
            matchPublishPort.publishMatchPort(match);
            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(matchId, matchPublishDto.getMatchId());
            assertEquals(0, matchPublishDto.getRound());
            assertFalse(matchPublishDto.getIsLastRound());
        }

        @Test
        @DisplayName("매치 중지 정보를 비동기 전송 한다.")
        void publishMatchEnd() throws InterruptedException {
            // arrange
            final long matchId = 1L;
            final String playerId = CommonUtil.randomId();
            final String name = "MONG-NAME";
            final String mongCode = "MONG-TYPE-CODE";
            final String mongName = "MONG-TYPE-NAME";
            final MatchPlayer matchPlayer = MatchPlayer.builder()
                    .playerId(playerId)
                    .deviceId(CommonUtil.randomId())
                    .accountId(1L)
                    .mongId(1L)
                    .mongCode(mongCode)
                    .mongName(mongName)
                    .name(name)
                    .attack(0D)
                    .heal(0D)
                    .defence(0D)
                    .isBot(false)
                    .hp(100D)
                    .isEnter(true)
                    .enteredAt(LocalDateTime.now())
                    .exitedAt(null)
                    .build();

            final List<MatchPlayer> matchPlayers = List.of(
                    matchPlayer,
                    MatchPlayer.builder()
                            .playerId(CommonUtil.randomId())
                            .deviceId(CommonUtil.randomId())
                            .accountId(2L)
                            .mongId(2L)
                            .mongCode("MONG-TYPE-CODE 2")
                            .mongName("MONG-TYPE-NAME 2")
                            .name("MONG-NAME 2")
                            .attack(0D)
                            .heal(0D)
                            .defence(0D)
                            .isBot(false)
                            .hp(100D)
                            .isEnter(true)
                            .enteredAt(LocalDateTime.now())
                            .exitedAt(null)
                            .build());

            final Match match = Match.builder()
                    .matchId(matchId)
                    .maxRound(10)
                    .matchPlayers(matchPlayers)
                    .round(1)
                    .stateCode(MatchStateCode.PROCESS)
                    .build();

            MatchPublishDto matchPublishDto = new MatchPublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            matchConsumer.reset(matchId, matchPublishDto, countDownLatch);

            // act
            matchPublishPort.publishMatchEndPort(match);
            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(matchId, matchPublishDto.getMatchId());
            assertEquals(1, matchPublishDto.getRound());
            assertFalse(matchPublishDto.getIsLastRound());
        }
    }
}