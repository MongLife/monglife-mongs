package com.monglife.mongs.domain.match.entity;

import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_match_player")
@ToString(exclude = "roundHistory")
public class MatchPlayerEntity {

    public static final Double MAX_HP = 5000D;
    public static final Double MAX_ATTACK_VALUE = 1000D;
    public static final Double MAX_HEAL_VALUE = 900D;
    public static final Double MAX_DEFENCE_VALUE = 750D;

    public static final Double DEFAULT_ATTACK_VALUE = 500D;
    public static final Double DEFAULT_HEAL_VALUE = 400D;
    public static final Double DEFAULT_DEFENCE_VALUE = 250D;

    @Id
    @Column(name = "player_id")
    private String playerId;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "mong_type_code")
    private String mongTypeCode;

    @Column(name = "hp")
    private Double hp = MAX_HP;

    @Column(name = "attack_value")
    private Double attackValue = MAX_ATTACK_VALUE;

    @Column(name = "heal_value")
    private Double healValue = MAX_HEAL_VALUE;

    @Column(name = "defence_value")
    private Double defenceValue = MAX_DEFENCE_VALUE;

    @Column(name = "is_bot")
    private Boolean isBot = Boolean.TRUE;

    @Column(name = "is_enter")
    private Boolean isEnter = Boolean.FALSE;

    @Column(name = "enter_dt")
    private LocalDateTime enterDt;

    @Column(name = "exit_dt")
    private LocalDateTime exitDt;

    @Transient
    private Double attackedValue = 0D;

    @Transient
    private Double healedValue = 0D;

    @Transient
    private Double defencedValue = 0D;

    @Transient
    private Set<MatchRoundCode> roundHistory = new HashSet<>();

    @Transient
    private MatchRoundCode roundCode = MatchRoundCode.NONE;


    @Builder
    public MatchPlayerEntity(String playerId, String deviceId, Long accountId, Long mongId, String mongTypeCode, Double attackValue, Double healValue, Double defenceValue, Boolean isBot) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
        this.attackValue = attackValue;
        this.healValue = healValue;
        this.defenceValue = defenceValue;
        this.isBot = isBot;

        // 봇인 경우 즉시 입장 처리
        if (this.isBot) {
            this.enter();
        }
    }

    /**
     * 배틀룸 입장
     */
    public void enter() {
        this.isEnter = Boolean.TRUE;
        this.enterDt = LocalDateTime.now();
    }

    /**
     * 배틀룸 중도 퇴장
     */
    public void duringRoundExit() {
        this.isEnter = Boolean.FALSE;
        this.exit();
    }

    /**
     * 배틀룸 퇴장
     */
    public void exit() {
        this.exitDt = LocalDateTime.now();
    }

    /**
     * 방어
     * @param defenceValue 방어 수치
     */
    public void defenced(Double defenceValue) {
        this.defencedValue = this.defencedValue + defenceValue;
        this.roundHistory.add(MatchRoundCode.MATCH_HISTORY_DEFENCED);
    }

    /**
     * 피해
     * @param damageValue 공격 대미지 수치
     */
    public void attacked(Double damageValue) {
        this.attackedValue = this.attackedValue + damageValue;
        this.roundHistory.add(MatchRoundCode.MATCH_HISTORY_ATTACKED);
    }

    /**
     * 회복
     * @param healValue 회복 수치
     */
    public void healed(Double healValue) {
        this.healedValue = this.healedValue + healValue;
        this.roundHistory.add(MatchRoundCode.MATCH_HISTORY_HEALED);
    }

    /**
     * 피해 & 회복 & 방어 수치 계산 및 라운드 상태 반환
     */
    public void next() {

        double totalAttackedValue = Math.max(0, this.attackedValue - this.defencedValue);
        double newHp = this.hp - totalAttackedValue + this.healedValue;
        this.hp = Math.max(0, Math.min(newHp, MAX_HP));

        if (roundHistory.contains(MatchRoundCode.MATCH_HISTORY_DEFENCED)) {
            roundCode = MatchRoundCode.MATCH_DEFENCE;
        } else if (roundHistory.contains(MatchRoundCode.MATCH_HISTORY_ATTACKED)) {
            if (roundHistory.contains(MatchRoundCode.MATCH_HISTORY_HEALED)) {
                roundCode = MatchRoundCode.MATCH_ATTACKED_HEAL;
            } else {
                roundCode = MatchRoundCode.MATCH_ATTACKED;
            }
        } else if (roundHistory.contains(MatchRoundCode.MATCH_HISTORY_HEALED)) {
            roundCode = MatchRoundCode.MATCH_HEAL;
        } else {
            roundCode = MatchRoundCode.NONE;
        }
    }
}
