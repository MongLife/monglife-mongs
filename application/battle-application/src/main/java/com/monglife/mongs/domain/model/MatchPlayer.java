package com.monglife.mongs.domain.model;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.domain.enums.BotMatchMongType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Random;

@Getter
@ToString
public class MatchPlayer {

    private static final Random random = new Random();
    // 최대 HP 수치
    private static final Double MAX_HP = 5000D;
    // 최대 공격 수치
    private static final Double MAX_ATTACK = 1000D;
    // 최대 회복 수치
    private static final Double MAX_HEAL = 900D;
    // 최대 수비 수치
    private static final Double MAX_DEFENCE = 750D;
    // 기본 공격 수치
    private static final Double DEFAULT_ATTACK = 500D;
    // 기본 회복 수치
    private static final Double DEFAULT_HEAL = 400D;
    // 기본 수비 수치
    private static final Double DEFAULT_DEFENCE = 250D;

    private final String playerId;

    private final String deviceId;

    private final Long accountId;

    private final Long mongId;

    private final String mongTypeCode;

    private final String mongTypeName;

    private final String mongName;

    private Double hp;

    private final Double attack;

    private final Double heal;

    private final Double defence;

    private final Boolean isBot;

    private Boolean isEnter;

    private LocalDateTime enteredDt;

    private LocalDateTime exitedDt;

    @Builder
    public MatchPlayer(String playerId, String deviceId, Long accountId, Long mongId, String mongTypeCode, String mongTypeName, String mongName, Double attack, Double heal, Double defence, Boolean isBot) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.mongName = mongName;
        this.hp = MAX_HP;
        this.attack = Math.min(attack, MAX_ATTACK);
        this.heal = Math.min(heal, MAX_HEAL);
        this.defence = Math.min(defence, MAX_DEFENCE);
        this.isBot = isBot;
        this.isEnter = Boolean.FALSE;
        this.enteredDt = null;
        this.exitedDt = null;
    }

    /**
     * 봇 매치 플레이어 생성
     * @return 봇 매치 플레이어 도메인 객체
     */
    public static MatchPlayer generateBotMatchPlayer() {

        double attack  = DEFAULT_ATTACK  + random.nextDouble(0, 100D);
        double heal    = DEFAULT_HEAL    + random.nextDouble(0, 100D);
        double defence = DEFAULT_DEFENCE + random.nextDouble(0, 100D);

        int mongTypeIndex = random.nextInt(0, BotMatchMongType.values().length);
        BotMatchMongType botMatchMongType = BotMatchMongType.values()[mongTypeIndex];

        String mongName = "봇 " + botMatchMongType.getMongTypeName();

        return MatchPlayer.builder()
                .playerId(CommonUtil.randomId())
                .deviceId(CommonUtil.randomId())
                .accountId(-1L)
                .mongId(-1L)
                .mongTypeCode(botMatchMongType.getMongTypeCode())
                .mongTypeName(botMatchMongType.getMongTypeName())
                .mongName(mongName)
                .attack(attack)
                .heal(heal)
                .defence(defence)
                .isBot(Boolean.TRUE)
                .build();
    }

    /**
     * 매치 플레이어 생성
     * @param queuePlayer 매치 대기열 도메인 객체
     * @param matchMong 매치 몽 도메인 객체
     * @return 매치 플레이어 도메인 객체
     */
    public static MatchPlayer generateMatchPlayer(QueuePlayer queuePlayer, MatchMong matchMong) {

        double attack  = DEFAULT_ATTACK  + matchMong.getStrength();
        double heal    = DEFAULT_HEAL    + matchMong.getFatigue();
        double defence = DEFAULT_DEFENCE + matchMong.getWeight();

        return MatchPlayer.builder()
                .playerId(CommonUtil.randomId())
                .deviceId(queuePlayer.getDeviceId())
                .accountId(matchMong.getAccountId())
                .mongId(matchMong.getMongId())
                .mongTypeCode(matchMong.getMongTypeCode())
                .mongTypeName(matchMong.getMongTypeName())
                .mongName(matchMong.getMongName())
                .attack(attack)
                .heal(heal)
                .defence(defence)
                .isBot(Boolean.FALSE)
                .build();
    }
}
