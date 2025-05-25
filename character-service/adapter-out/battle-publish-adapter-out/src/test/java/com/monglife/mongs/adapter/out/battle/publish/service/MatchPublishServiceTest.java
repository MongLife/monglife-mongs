package com.monglife.mongs.adapter.out.battle.publish.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.mqtt.config.MqttAutoConfig;
import com.monglife.mongs.adapter.out.battle.publish.config.AdapterOutBattlePublishConfig;
import com.monglife.mongs.adapter.out.battle.publish.consumer.MatchConsumer;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchEndPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchPublishDto;
import com.monglife.mongs.application.battle.port.out.MatchPublishPort;
import com.monglife.mongs.domain.battle.enums.MatchRoundCode;
import com.monglife.mongs.domain.battle.enums.MatchStateCode;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
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
class MatchPublishServiceTest {

    private final MatchPublishPort matchPublishPort;

    @Autowired
    public MatchPublishServiceTest(MatchPublishPort matchPublishPort) {
        this.matchPublishPort = matchPublishPort;
    }

    @Nested
    @DisplayName("매치 비동기 응답 단위 테스트")
    class PublishMatchPort {

        private final MatchConsumer matchConsumer;

        @Autowired
        public PublishMatchPort(MatchConsumer matchConsumer) {
            this.matchConsumer = matchConsumer;
        }

        @Test
        @DisplayName("매치 정보를 비동기 전송 한다.")
        void publishMatch() throws InterruptedException {
            // arrange
            long matchId = 1L;
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
                    .matchId(matchId)
                    .maxRound(10)
                    .matchPlayers(matchPlayers)
                    .round(0)
                    .matchStateCode(MatchStateCode.PROCESS)
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
            long matchId = 1L;
            String playerId = CommonUtil.randomId();
            String mongName = "MONG-NAME";
            String mongTypeCode = "MONG-TYPE-CODE";
            String mongTypeName = "MONG-TYPE-NAME";
            MatchPlayer matchPlayer = MatchPlayer.builder()
                    .playerId(playerId)
                    .deviceId(CommonUtil.randomId())
                    .accountId(1L)
                    .mongId(1L)
                    .mongTypeCode(mongTypeCode)
                    .mongTypeName(mongTypeName)
                    .mongName(mongName)
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
                    .build();

            List<MatchPlayer> matchPlayers = List.of(
                    matchPlayer,
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
                    .matchId(matchId)
                    .maxRound(10)
                    .matchPlayers(matchPlayers)
                    .round(1)
                    .matchStateCode(MatchStateCode.PROCESS)
                    .build();

            MatchEndPublishDto matchEndPublishDto = new MatchEndPublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            matchConsumer.reset(matchId, matchEndPublishDto, countDownLatch);

            // act
            matchPublishPort.publishMatchEndPort(match, matchPlayer);
            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(matchId, matchEndPublishDto.getMatchId());
            assertEquals(playerId, matchEndPublishDto.getPlayerId());
            assertEquals(mongName, matchEndPublishDto.getMongName());
            assertEquals(mongTypeCode, matchEndPublishDto.getMongTypeCode());
            assertEquals(mongTypeName, matchEndPublishDto.getMongTypeName());
        }
    }
}