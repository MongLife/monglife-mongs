package com.monglife.mongs.domain.battle.model;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.domain.battle.enums.BotMatchMongType;
import com.monglife.mongs.domain.battle.enums.MatchHistoryCode;
import com.monglife.mongs.domain.battle.enums.MatchRoundCode;
import com.monglife.mongs.domain.battle.exception.AlreadyEnterMatchPlayerException;
import com.monglife.mongs.domain.battle.exception.AlreadyExitMatchPlayerException;
import com.monglife.mongs.domain.battle.vo.GenerateMatchPlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

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
    // 최대 방어 수치
    private static final Double MAX_DEFENCE = 750D;
    // 기본 공격 수치
    private static final Double DEFAULT_ATTACK = 500D;
    // 기본 회복 수치
    private static final Double DEFAULT_HEAL = 400D;
    // 기본 방어 수치
    private static final Double DEFAULT_DEFENCE = 250D;

    /**
     * persist field
     */
    private final String playerId;

    private final String deviceId;

    private final Long accountId;

    private final Long mongId;

    private final String mongCode;

    private final String mongName;

    private final String name;

    private final Double attack;

    private final Double heal;

    private final Double defence;

    private final Boolean isBot;

    private Double hp;

    private Boolean isEnter;

    private LocalDateTime enteredAt;

    private LocalDateTime exitedAt;

    /**
     * local field
     */
    private Double damage;

    private Double recovery;

    private MatchRoundCode roundCode;

    private Set<MatchHistoryCode> historyCodes;

    @Builder
    public MatchPlayer(String playerId, String deviceId, Long accountId, Long mongId, String mongCode, String mongName, String name, Double attack, Double heal, Double defence, Boolean isBot, Double hp, Boolean isEnter, LocalDateTime enteredAt, LocalDateTime exitedAt) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongCode = mongCode;
        this.mongName = mongName;
        this.name = name;
        this.attack = Math.min(attack, MAX_ATTACK);
        this.heal = Math.min(heal, MAX_HEAL);
        this.defence = Math.min(defence, MAX_DEFENCE);
        this.isBot = isBot;
        this.hp = Math.min(hp, MAX_HP);
        this.isEnter = isEnter;
        this.enteredAt = enteredAt;
        this.exitedAt = exitedAt;
        this.damage = 0D;
        this.recovery = 0D;
        this.roundCode = MatchRoundCode.NONE;
        this.historyCodes = new HashSet<>();
    }

    /**
     * 입장
     */
    public void enter() {
        if (Boolean.TRUE.equals(this.isEnter)) {
            throw new AlreadyEnterMatchPlayerException();
        }

        this.isEnter = Boolean.TRUE;
        this.enteredAt = LocalDateTime.now();
    }

    /**
     * 퇴장
     */
    public void exit() {
        if (Boolean.FALSE.equals(this.isEnter)) {
            throw new AlreadyExitMatchPlayerException();
        }

        this.isEnter = Boolean.FALSE;
        this.exitedAt = LocalDateTime.now();
    }

    /**
     * 방어
     */
    public void defence() {
        this.damage = this.damage - this.defence;
        this.historyCodes.add(MatchHistoryCode.MATCH_HISTORY_DEFENCED);
    }

    /**
     * 피해
     * @param damage 피해 수치
     */
    public void damage(Double damage) {
        this.damage = this.damage + damage;
        this.historyCodes.add(MatchHistoryCode.MATCH_HISTORY_ATTACKED);
    }

    /**
     * 회복
     * @param recovery 회복 수치
     */
    public void heal(Double recovery) {
        this.recovery = this.recovery + recovery;
        this.historyCodes.add(MatchHistoryCode.MATCH_HISTORY_HEALED);
    }

    /**
     * 피해, 회복 수치 적용
     */
    public void applyDamageAndRecovery() {
        // 피해 수치 적용
        this.hp = this.hp - Math.max(0, this.damage);
        // 회복 수치 적용
        this.hp = this.hp + Math.max(0, this.recovery);
        // HP 범위 체크
        this.hp = Math.max(0, Math.min(this.hp, MAX_HP));

        if (this.historyCodes.contains(MatchHistoryCode.MATCH_HISTORY_DEFENCED)) {
            // 방어
            this.roundCode = MatchRoundCode.MATCH_DEFENCE;
        } else if (this.historyCodes.contains(MatchHistoryCode.MATCH_HISTORY_ATTACKED)) {
            if (this.historyCodes.contains(MatchHistoryCode.MATCH_HISTORY_HEALED)) {
                // 공격 + 회복
                this.roundCode = MatchRoundCode.MATCH_ATTACKED_HEAL;
            } else {
                // 공격
                this.roundCode = MatchRoundCode.MATCH_ATTACKED;
            }
        } else if (this.historyCodes.contains(MatchHistoryCode.MATCH_HISTORY_HEALED)) {
            // 회복
            this.roundCode = MatchRoundCode.MATCH_HEAL;
        } else {
            // 변동 없음
            this.roundCode = MatchRoundCode.NONE;
        }

        // 피해, 회복 수치 초기화
        this.damage = 0D;
        this.recovery = 0D;
        this.historyCodes = new HashSet<>();
    }

    /**
     * 매치 플레이어 사망 여부 조회
     * @return 매치 플레이어 사망 여부
     */
    public Boolean isDead() {
        return this.hp <= 0;
    }

    /**
     * 봇 매치 플레이어 다건 생성
     * @return 봇 매치 플레이어 도메인 객체
     */
    public static List<MatchPlayer> generateBotMatchPlayers(Integer count) {

        List<MatchPlayer> matchPlayers = new ArrayList<>();

        for (long index = 1; index <= count; index++) {

            double attack  = DEFAULT_ATTACK  + random.nextDouble(0, 50D);
            double heal    = DEFAULT_HEAL    + random.nextDouble(0, 50D);
            double defence = DEFAULT_DEFENCE + random.nextDouble(0, 50D);

            int mongTypeIndex = random.nextInt(0, BotMatchMongType.values().length);
            BotMatchMongType botMatchMongType = BotMatchMongType.values()[mongTypeIndex];

            String name = "봇 " + botMatchMongType.getMongName();

            MatchPlayer matchPlayer = MatchPlayer.builder()
                    .playerId(CommonUtil.randomId())
                    .deviceId(CommonUtil.randomId())
                    .accountId(-index)
                    .mongId(-index)
                    .mongCode(botMatchMongType.getMongCode())
                    .mongName(botMatchMongType.getMongName())
                    .name(name)
                    .attack(attack)
                    .heal(heal)
                    .defence(defence)
                    .isBot(Boolean.TRUE)
                    .hp(MAX_HP)
                    .isEnter(Boolean.TRUE)
                    .enteredAt(LocalDateTime.now())
                    .build();

            matchPlayers.add(matchPlayer);
        }

        return matchPlayers;
    }

    /**
     * 매치 플레이어 생성
     * @param queuePlayer 매치 대기열 도메인 객체
     * @param generateMatchPlayerVo 플레이어 생성 정보 Vo
     * @return 매치 플레이어 도메인 객체
     */
    public static MatchPlayer generateMatchPlayer(QueuePlayer queuePlayer, GenerateMatchPlayerVo generateMatchPlayerVo) {

        double attack  = DEFAULT_ATTACK  + generateMatchPlayerVo.getStrength();
        double heal    = DEFAULT_HEAL    + generateMatchPlayerVo.getFatigue();
        double defence = DEFAULT_DEFENCE + generateMatchPlayerVo.getWeight();

        return MatchPlayer.builder()
                .playerId(CommonUtil.randomId())
                .deviceId(queuePlayer.getDeviceId())
                .accountId(generateMatchPlayerVo.getAccountId())
                .mongId(generateMatchPlayerVo.getMongId())
                .mongCode(generateMatchPlayerVo.getMongCode())
                .mongName(generateMatchPlayerVo.getMongName())
                .name(generateMatchPlayerVo.getName())
                .attack(attack)
                .heal(heal)
                .defence(defence)
                .isBot(Boolean.FALSE)
                .hp(MAX_HP)
                .isEnter(Boolean.FALSE)
                .build();
    }
}
