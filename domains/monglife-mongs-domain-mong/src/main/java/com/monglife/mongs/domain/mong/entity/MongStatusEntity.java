package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.listener.MongStateEntityListener;
import com.monglife.mongs.domain.mong.listener.MongStatusEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class, MongStatusEntityListener.class })
@Table(name = "mongs_mong_status")
@ToString(exclude = { "history", "mong" })
public class MongStatusEntity extends BaseTimeEntity {

    // 최대 배변 개수
    protected static final Integer MAX_POOP_COUNT = 4;

    // 아픔 상태 돌입 체력 지수 비율 (퍼센트)
    private static final Double HEALTH_SICK_RATIO = 10D;

    // 배고픔 상태 돌입 포만감 지수 비율
    private static final Double SATIETY_HUNGRY_RATIO = 10D;

    // 피곤함 상태 돌입 피로도 지수 비율
    private static final Double FATIGUE_SOMNOLENCE_RATIO = 10D;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_status_id")
    private Long mongStatusId;

    @OneToOne(mappedBy = "status", cascade = CascadeType.PERSIST)
    private MongEntity mong;

    @Column(name = "max_status")
    private Double maxStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_status_code")
    private MongStatusCode code;

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

    @Column(name = "exp_ratio")
    private Double expRatio;

    @Column(name = "strength_ratio")
    private Double strengthRatio;

    @Column(name = "satiety_ratio")
    private Double satietyRatio;

    @Column(name = "healthy_ratio")
    private Double healthyRatio;

    @Column(name = "fatigue_ratio")
    private Double fatigueRatio;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_status_id")
    private List<MongStatusHistoryEntity> history = new ArrayList<>();

    public MongStatusEntity(Double maxStatus) {
        this.maxStatus = maxStatus;
        this.code = MongStatusCode.NORMAL;
        this.weight = 0D;
        this.poopCount = 0;

        this.exp = 0D;
        this.strength = maxStatus;
        this.satiety = maxStatus;
        this.healthy = maxStatus;
        this.fatigue = maxStatus;

        this.expRatio = 0D;
        this.strengthRatio = 100D;
        this.satietyRatio = 100D;
        this.healthyRatio = 100D;
        this.fatigueRatio = 100D;
    }

    /**
     * 지수 갱신 (증감 연산)
     * @param weight 몸무게 변동 값
     * @param poopCount 배변 수 변동 값
     * @param exp 경험치 변동 값
     * @param strength 힘 변동 값
     * @param satiety 포만감 변동 값
     * @param healthy 체력 변동 값
     * @param fatigue 피로도 변동 값
     */
    public void patchStatus(Double weight, Integer poopCount, Double exp, Double strength, Double satiety, Double healthy, Double fatigue) {

        this.weight = this.weight + weight;
        this.poopCount = this.poopCount + poopCount;
        this.exp = this.exp + exp;
        this.strength = this.strength + strength;
        this.satiety = this.satiety + satiety;
        this.healthy = this.healthy + healthy;
        this.fatigue = this.fatigue + fatigue;

        this.weight = Math.max(0D, this.weight);
        this.poopCount = Math.max(0, Math.min(this.poopCount, MAX_POOP_COUNT));
        this.exp = Math.max(0D, Math.min(this.exp, this.maxStatus));
        this.expRatio = this.exp / this.maxStatus * 100;
        this.syncStatusValueToStatusRatio();

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.PATCH_STATUS);
    }

    /**
     * 경험치 증가
     * @param exp 경험치
     */
    public void increaseExp(Double exp) {

        if (0D >= exp) return;

        this.exp = this.exp + exp;

        this.exp = Math.max(0D, Math.min(this.exp, this.maxStatus));
        this.expRatio = this.exp / this.maxStatus * 100;

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.INCREASE_EXP);
    }

    /**
     * 경험치 감소
     * @param exp 경험치
     */
    public void decreaseExp(Double exp) {

        if (0D >= this.exp|| 0D >= exp) return;

        this.exp = this.exp - exp;

        this.exp = Math.max(0D, Math.min(this.exp, this.maxStatus));
        this.expRatio = this.exp / this.maxStatus * 100;

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.DECREASE_EXP);
    }

    /**
     * 경험치 초기화
     */
    public void resetExp() {

        if (0D >= this.exp) return;

        this.exp = 0D;

        this.exp = Math.max(0D, Math.min(this.exp, this.maxStatus));
        this.expRatio = this.exp / this.maxStatus * 100;

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.RESET_EXP);
    }

    /**
     * 배변 수 증가
     * @param poopCount 배변 수
     */
    public void increasePoopCount(Integer poopCount) {

        if (0 >= poopCount) return;

        this.poopCount = this.poopCount + poopCount;

        this.poopCount = Math.max(0, Math.min(this.poopCount, MAX_POOP_COUNT));

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.INCREASE_POOP_COUNT);
    }

    /**
     * 배변 수 감소
     * @param poopCount 배변 수
     */
    public void decreasePoopCount(Integer poopCount) {

        if (0 >= this.poopCount || 0 >= poopCount) return;

        this.poopCount = this.poopCount - poopCount;

        this.poopCount = Math.max(0, Math.min(this.poopCount, MAX_POOP_COUNT));

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.DECREASE_POOP_COUNT);
    }

    /**
     * 배변 수 초기화
     */
    public void resetPoopCount() {

        if (0 >= this.poopCount) return;

        this.poopCount = 0;

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.RESET_POOP_COUNT);
    }

    /**
     * 몸무게 증가
     * @param weight 몸무게
     */
    public void increaseWeight(Double weight) {

        if (0 >= weight) return;

        this.weight = this.weight + weight;

        this.weight = Math.max(0D, this.weight);

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.INCREASE_WEIGHT);
    }

    /**
     * 몸무게 감소
     * @param weight 몸무게
     */
    public void decreaseWeight(Double weight) {

        if (0D >= this.weight || 0D >= weight) return;

        this.weight = this.weight - weight;

        this.weight = Math.max(0D, this.weight);

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.DECREASE_WEIGHT);
    }

    /**
     * 지수 증가
     * @param strength 힘
     * @param satiety 포만감
     * @param healthy 체력
     * @param fatigue 피로도
     */
    public void increaseStatus(Double strength, Double satiety, Double healthy, Double fatigue) {

        if (this.maxStatus <= this.strength && this.maxStatus <= this.satiety && this.maxStatus <= this.healthy && this.maxStatus <= this.fatigue) {
            return;
        }

        this.strength = this.strength + strength;
        this.satiety = this.satiety + satiety;
        this.healthy = this.healthy + healthy;
        this.fatigue = this.fatigue + fatigue;
        this.syncStatusValueToStatusRatio();

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.INCREASE_STATUS);
    }

    /**
     * 지수 감소
     * @param strength 힘
     * @param satiety 포만감
     * @param healthy 체력
     * @param fatigue 피로도
     */
    public void decreaseStatus(Double strength, Double satiety, Double healthy, Double fatigue) {

        if (0D >= strength && 0D >= satiety && 0D >= healthy && 0D >= fatigue) return;

        if (0D >= this.strength && 0D >= this.satiety && 0D >= this.healthy && 0D >= this.fatigue) return;

        this.strength = this.strength - strength;
        this.satiety = this.satiety - satiety;
        this.healthy = this.healthy - healthy;
        this.fatigue = this.fatigue - fatigue;
        this.syncStatusValueToStatusRatio();

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.DECREASE_STATUS);
    }

    /**
     * 지수 퍼센트 증가
     * @param strengthRatio 힘 퍼센트
     * @param satietyRatio 포만감 퍼센트
     * @param healthyRatio 체력 퍼센트
     * @param fatigueRatio 피로도 퍼센트
     */
    public void increaseStatusRatio(Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio) {

        if (100D <= this.strengthRatio && 100D <= this.satietyRatio && 100D <= this.healthyRatio && 100D <= this.fatigueRatio) {
            return;
        }

        this.strengthRatio = this.strengthRatio + strengthRatio;
        this.satietyRatio = this.satietyRatio + satietyRatio;
        this.healthyRatio = this.healthyRatio + healthyRatio;
        this.fatigueRatio = this.fatigueRatio + fatigueRatio;
        this.syncStatusRatioToStatusValue();

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.INCREASE_STATUS_RATIO);
    }

    /**
     * 지수 퍼센트 감소
     * @param strengthRatio 힘 퍼센트
     * @param satietyRatio 포만감 퍼센트
     * @param healthyRatio 체력 퍼센트
     * @param fatigueRatio 피로도 퍼센트
     */
    public void decreaseStatusRatio(Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio) {

        if (0D >= strengthRatio && 0D >= satietyRatio && 0D >= healthyRatio && 0D >= fatigueRatio) return;

        if (0D >= this.strengthRatio && 0D >= this.satietyRatio && 0D >= this.healthyRatio && 0D >= this.fatigueRatio) return;

        this.strengthRatio = this.strengthRatio - strengthRatio;
        this.satietyRatio = this.satietyRatio - satietyRatio;
        this.healthyRatio = this.healthyRatio - healthyRatio;
        this.fatigueRatio = this.fatigueRatio - fatigueRatio;
        this.syncStatusRatioToStatusValue();

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.DECREASE_STATUS_RATIO);
    }

    /**
     * 지수 최대 수치 변경
     * @param maxStatus 지수 최대 수치
     */
    public void setMaxStatus(Double maxStatus) {

        if (this.maxStatus.equals(maxStatus)) return;

        this.exp = this.exp / this.maxStatus * maxStatus;
        this.strength = this.strength / this.maxStatus * maxStatus;
        this.satiety = this.satiety / this.maxStatus * maxStatus;
        this.healthy = this.healthy / this.maxStatus * maxStatus;
        this.fatigue = this.fatigue / this.maxStatus * maxStatus;

        this.maxStatus = maxStatus;

        this.exp = Math.max(0D, Math.min(this.exp, this.maxStatus));
        this.expRatio = this.exp / this.maxStatus * 100;

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.SET_MAX_STATUS);
    }

    /**
     * 지수 코드 변경
     * @param code 코드
     */
    public void setCode(MongStatusCode code) {

        if (this.code == code) return;

        this.code = code;

        this.addHistory(MongStatusHistoryEntity.MongStatusHistoryType.SET_CODE);
    }

    /**
     * 지수 비율 -> 지수 수치 동기화
     * ex) 퍼센트 증가 후, 지수 수치 갱신
     */
    private void syncStatusRatioToStatusValue() {
        this.strengthRatio = Math.max(0D, Math.min(this.strengthRatio, 100D));
        this.satietyRatio = Math.max(0D, Math.min(this.satietyRatio, 100D));
        this.healthyRatio = Math.max(0D, Math.min(this.healthyRatio, 100D));
        this.fatigueRatio = Math.max(0D, Math.min(this.fatigueRatio, 100D));

        this.strength = this.strengthRatio * this.maxStatus / 100;
        this.satiety = this.satietyRatio * this.maxStatus / 100;
        this.healthy = this.healthyRatio * this.maxStatus / 100;
        this.fatigue = this.fatigueRatio * this.maxStatus / 100;

        this.syncStatusCode();
    }

    /**
     * 지수 수치 -> 지수 비율 동기화
     * ex) 지수 수치 증가 후, 퍼센트 갱신
     */
    private void syncStatusValueToStatusRatio() {
        this.strength = Math.max(0D, Math.min(this.strength, this.maxStatus));
        this.satiety = Math.max(0D, Math.min(this.satiety, this.maxStatus));
        this.healthy = Math.max(0D, Math.min(this.healthy, this.maxStatus));
        this.fatigue = Math.max(0D, Math.min(this.fatigue, this.maxStatus));

        this.strengthRatio = this.strength / this.maxStatus * 100;
        this.satietyRatio = this.satiety / this.maxStatus * 100;
        this.healthyRatio = this.healthy / this.maxStatus * 100;
        this.fatigueRatio = this.fatigue / this.maxStatus * 100;

        this.syncStatusCode();
    }

    /**
     * 지수에 따른 코드 변경
     */
    public void syncStatusCode() {
        // 지수 코드 변경 조건 확인
        if (this.getHealthyRatio() <= HEALTH_SICK_RATIO) {
            this.setCode(MongStatusCode.SICK);
        } else if (this.getSatiety() <= SATIETY_HUNGRY_RATIO) {
            this.setCode(MongStatusCode.HUNGRY);
        } else if (this.getFatigue() <= FATIGUE_SOMNOLENCE_RATIO) {
            this.setCode(MongStatusCode.SOMNOLENCE);
        } else {
            this.setCode(MongStatusCode.NORMAL);
        }
    }

    private void addHistory(MongStatusHistoryEntity.MongStatusHistoryType mongStatusHistoryType) {

        this.history.add(MongStatusHistoryEntity.builder()
                .mongId(this.mong.getMongId())
                .accountId(this.mong.getAccountId())
                .mongName(this.mong.getMongName())
                .mongStatusHistoryType(mongStatusHistoryType)
                .maxStatus(this.maxStatus)
                .code(this.code)
                .weight(this.weight)
                .poopCount(this.poopCount)
                .exp(this.exp)
                .strength(this.strength)
                .satiety(this.satiety)
                .healthy(this.healthy)
                .fatigue(this.fatigue)
                .expRatio(this.expRatio)
                .strengthRatio(this.strengthRatio)
                .satietyRatio(this.satietyRatio)
                .healthyRatio(this.healthyRatio)
                .fatigueRatio(this.fatigueRatio)
                .build());
    }
}
