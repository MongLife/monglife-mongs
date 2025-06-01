package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.model.Mong;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_mong")
public class MongEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "sleep_at")
    private LocalTime sleepAt;

    @Column(name = "wakeup_at")
    private LocalTime wakeupAt;

    @Column(name = "pay_point")
    protected Integer payPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mong_type_id")
    private MongTypeEntity mongType;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_code")
    private MongStateCode stateCode;

    @Column(name = "is_sleep")
    private Boolean isSleep;

    @Column(name = "max_status")
    private Double maxStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code")
    private MongStatusCode statusCode;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "poop_count")
    private Integer poopCount;

    @Column(name = "exp")
    private Double exp;

    @Column(name = "strength")
    private Double strength;

    @Column(name = "satiety")
    private Double satiety;

    @Column(name = "healthy")
    private Double healthy;

    @Column(name = "fatigue")
    private Double fatigue;

    @Column(name = "training_count")
    private Integer trainingCount;

    @Column(name = "stroke_count")
    private Integer strokeCount;

    @Column(name = "random_draw_ticket_count")
    private Integer randomDrawTicketCount;

    @Column(name = "reward")
    private Double evolutionReward;

    @Column(name = "penalty")
    private Double evolutionPenalty;

    @Builder
    public MongEntity(Long mongId, Long accountId, String name, LocalTime sleepAt, LocalTime wakeupAt, Integer payPoint, MongTypeEntity mongType, MongStateCode stateCode, Boolean isSleep, Double maxStatus, MongStatusCode statusCode, Double weight, Integer poopCount, Double exp, Double strength, Double satiety, Double healthy, Double fatigue, Integer trainingCount, Integer strokeCount, Integer randomDrawTicketCount, Double evolutionReward, Double evolutionPenalty) {
        this.mongId = mongId;
        this.accountId = accountId;
        this.name = name;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
        this.payPoint = payPoint;
        this.mongType = mongType;
        this.stateCode = stateCode;
        this.isSleep = isSleep;
        this.maxStatus = maxStatus;
        this.statusCode = statusCode;
        this.weight = weight;
        this.poopCount = poopCount;
        this.exp = exp;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.trainingCount = trainingCount;
        this.strokeCount = strokeCount;
        this.randomDrawTicketCount = randomDrawTicketCount;
        this.evolutionReward = evolutionReward;
        this.evolutionPenalty = evolutionPenalty;
    }

    /**
     * 엔티티 도메인 변환
     * @return 몽 도메인 객체
     */
    public Mong toDomain() {
        return Mong.builder()
                .mongId(this.mongId)
                .accountId(this.accountId)
                .name(this.name)
                .mongCode(this.mongType.getComn().getCode())
                .mongName(this.mongType.getComn().getName())
                .statusCode(this.statusCode)
                .stateCode(this.stateCode)
                .level(this.mongType.getLevel())
                .maxStatus(this.maxStatus)
                .sleepAt(this.sleepAt)
                .wakeupAt(this.wakeupAt)
                .payPoint(this.payPoint)
                .isSleep(this.isSleep)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .exp(this.exp)
                .weight(this.weight)
                .evolutionReward(this.evolutionReward)
                .evolutionPenalty(this.evolutionPenalty)
                .strokeCount(this.strokeCount)
                .trainingCount(this.trainingCount)
                .poopCount(this.poopCount)
                .randomDrawTicketCount(this.randomDrawTicketCount)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }

    /**
     * 몽 수정
     * @param mong 몽 도메인 객체
     */
    public void update(Mong mong) {
        this.update(mong, this.mongType);
    }

    /**
     * 몽 수정
     * @param mong 몽 도메인 객체
     * @param mongTypeEntity 변경될 몽 타입 엔티티
     */
    public void update(Mong mong, MongTypeEntity mongTypeEntity) {
        this.accountId = mong.getAccountId();
        this.name = mong.getName();
        this.sleepAt = mong.getSleepAt();
        this.wakeupAt = mong.getWakeupAt();
        this.payPoint = mong.getPayPoint();
        this.mongType = mongTypeEntity;
        this.stateCode = mong.getStateCode();
        this.isSleep = mong.getIsSleep();
        this.maxStatus = mong.getMaxStatus();
        this.statusCode = mong.getStatusCode();
        this.weight = mong.getWeight();
        this.poopCount = mong.getPoopCount();
        this.exp = mong.getExp();
        this.strength = mong.getStrength();
        this.satiety = mong.getSatiety();
        this.healthy = mong.getHealthy();
        this.fatigue = mong.getFatigue();
        this.trainingCount = mong.getTrainingCount();
        this.strokeCount = mong.getStrokeCount();
        this.randomDrawTicketCount = mong.getRandomDrawTicketCount();
        this.evolutionReward = mong.getEvolutionReward();
        this.evolutionPenalty = mong.getEvolutionPenalty();
    }
}
