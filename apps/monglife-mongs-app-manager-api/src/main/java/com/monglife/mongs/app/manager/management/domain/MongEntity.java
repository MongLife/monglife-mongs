package com.monglife.mongs.app.manager.management.domain;

import com.monglife.mongs.app.manager.management.dto.etc.UpdateMongStatusDto;
import com.monglife.mongs.app.manager.management.enums.MongShiftCode;
import com.monglife.mongs.app.manager.management.enums.MongStateCode;
import com.monglife.mongs.module.jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mongs_manager_mong")
public class MongEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "mong_name")
    private String mongName;

    @Column(name = "sleep_at")
    private LocalTime sleepAt;

    @Column(name = "wakeup_at")
    private LocalTime wakeupAt;

    @Column(name = "pay_point")
    private Integer payPoint;

    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "isSleep")
    private Boolean isSleep = Boolean.FALSE;

    @Column(name = "isTimeLimit")
    private Boolean isTimeLimit = Boolean.FALSE;

    @Column(name = "training_count")
    private Integer trainingCount = 0;

    @Column(name = "stroke_count")
    private Integer strokeCount = 0;

    @Column(name = "reward")
    private Double reward = 0D;

    @Column(name = "penalty")
    private Double penalty = 0D;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mong_type_code", referencedColumnName = "mong_type_code")
    private MongTypeEntity type;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_state_code")
    private MongStateCode mongStateCode = MongStateCode.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_shift_code")
    private MongShiftCode mongShiftCode = MongShiftCode.NORMAL;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_status_id")
    private MongStatusEntity mongStatus;


    @Builder
    public MongEntity(Long accountId, String mongName, LocalTime sleepAt, LocalTime wakeupAt, MongTypeEntity type, Integer payPoint) {
        this.accountId = accountId;
        this.mongName = mongName;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
        this.type = type;
        this.payPoint = payPoint;
        this.mongStatus = MongStatusEntity.builder()
                .strength(this.type.getMaxStatus())
                .satiety(this.type.getMaxStatus())
                .healthy(this.type.getMaxStatus())
                .fatigue(this.type.getMaxStatus())
                .strengthRatio(100D)
                .satietyRatio(100D)
                .healthyRatio(100D)
                .fatigueRatio(100D)
                .build();
    }

    /**
     * 삭제
     */
    public void delete() {

        this.isActive = Boolean.FALSE;
        this.isSleep = Boolean.FALSE;
        this.isTimeLimit = Boolean.FALSE;
        this.mongShiftCode = MongShiftCode.NORMAL;
        this.mongStateCode = MongStateCode.NORMAL;
    }

    /**
     * 쓰다 듬기
     * @param addExp 쓰다 듬기 경험치
     */
    public void stroke(Double addExp) {

        final Double maxStatus = this.type.getMaxStatus();

        this.mongStatus.exp = Math.min(maxStatus, this.mongStatus.getExp() + addExp);
        this.strokeCount = this.strokeCount + 1;
    }

    /**
     * 수면
     */
    public void sleep() {
        this.isSleep = Boolean.TRUE;
    }

    /**
     * 기상
     */
    public void wakeup() {
        this.isSleep = Boolean.FALSE;
    }

    /**
     * 몽 배변 처리
     */
    public void poopClean(Double poopCleanExp) {
        this.mongStatus.poopCount = 0;
        this.mongStatus.exp = this.mongStatus.exp + poopCleanExp;
    }

    /**
     * 몽 먹이 주기
     * @param updateMongStatusDto 음식 종류에 따른 스텟 변경 수치 DTO
     */
    public void feed(UpdateMongStatusDto updateMongStatusDto) {

        final Double maxStatus = this.type.getMaxStatus();

        double weight = Math.min(maxStatus, this.mongStatus.weight + updateMongStatusDto.getAddWeightValue());
        double strength = Math.min(maxStatus, this.mongStatus.strength + updateMongStatusDto.getAddStrengthValue());
        double satiety = Math.min(maxStatus, this.mongStatus.satiety + updateMongStatusDto.getAddSatietyValue());
        double healthy = Math.min(maxStatus, this.mongStatus.healthy + updateMongStatusDto.getAddHealthyValue());
        double fatigue = Math.min(maxStatus, this.mongStatus.fatigue + updateMongStatusDto.getAddFatigueValue());

        this.mongStatus.weight = weight;
        this.mongStatus.strength = strength;
        this.mongStatus.satiety = satiety;
        this.mongStatus.healthy = healthy;
        this.mongStatus.fatigue = fatigue;

        this.mongStatus.weightRatio = weight / maxStatus * 100;
        this.mongStatus.strengthRatio = strength / maxStatus * 100;
        this.mongStatus.satietyRatio = satiety / maxStatus * 100;
        this.mongStatus.healthyRatio = healthy / maxStatus * 100;
        this.mongStatus.fatigueRatio = fatigue / maxStatus * 100;
    }

    /**
     * 몽 진화 준비
     */
    public void evolutionReady() {

        this.mongShiftCode = MongShiftCode.EVOLUTION_READY;
        this.mongStateCode = MongStateCode.NORMAL;
    }

    /**
     * 몽 진화
     * @param nextMongTypeEntity 다음 몽 타입
     */
    public void evolution(MongTypeEntity nextMongTypeEntity) {

        final Double maxStatus = this.type.getMaxStatus();
        final Double nextMaxStatus = nextMongTypeEntity.getMaxStatus();

        double weight = this.mongStatus.weight / maxStatus * nextMaxStatus;
        double strength = this.mongStatus.strength / maxStatus * nextMaxStatus;
        double satiety = this.mongStatus.satiety / maxStatus * nextMaxStatus;
        double healthy = this.mongStatus.healthy / maxStatus * nextMaxStatus;
        double fatigue = this.mongStatus.fatigue / maxStatus * nextMaxStatus;

        this.mongStatus.weight = weight;
        this.mongStatus.strength = strength;
        this.mongStatus.satiety = satiety;
        this.mongStatus.healthy = healthy;
        this.mongStatus.fatigue = fatigue;

        this.type = nextMongTypeEntity;
    }

    /**
     * 몽 졸업 준비
     */
    public void graduateReady() {

        final Double maxStatus = this.type.getMaxStatus();

        this.mongStatus.weight = maxStatus;
        this.mongStatus.strength = maxStatus;
        this.mongStatus.satiety = maxStatus;
        this.mongStatus.healthy = maxStatus;
        this.mongStatus.fatigue = maxStatus;

        this.isSleep = Boolean.FALSE;
        this.isTimeLimit = Boolean.FALSE;
        this.mongShiftCode = MongShiftCode.GRADUATE_READY;
        this.mongStateCode = MongStateCode.NORMAL;
    }

    /**
     * 몽 졸업
     */
    public void graduate() {

        this.isSleep = Boolean.FALSE;
        this.isTimeLimit = Boolean.FALSE;
        this.mongShiftCode = MongShiftCode.NORMAL;
        this.mongStateCode = MongStateCode.NORMAL;
    }

    /**
     * 몽 사망
     */
    public void dead() {

        this.isSleep = Boolean.FALSE;
        this.isTimeLimit = Boolean.FALSE;
        this.mongShiftCode = MongShiftCode.DEAD;
        this.mongStateCode = MongStateCode.NORMAL;
    }
}
