package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.dto.etc.DecreaseMongStatusRatioDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusRatioDto;
import com.monglife.mongs.domain.mong.dto.etc.PatchMongStatusDto;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.listener.MongEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class, MongEntityListener.class })
@Table(name = "mongs_mong")
@ToString(exclude = { "history", "meta", "state", "status" })
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
    protected Integer payPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mong_type_id")
    private MongTypeEntity type;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_id")
    private List<MongHistoryEntity> history = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_meta_id")
    private MongMetaEntity meta;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_state_id")
    private MongStateEntity state;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_status_id")
    private MongStatusEntity status;

    @Builder
    public MongEntity(Long accountId, String mongName, LocalTime sleepAt, LocalTime wakeupAt, MongTypeEntity type, Integer payPoint) {
        this.accountId = accountId;
        this.mongName = mongName;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
        this.payPoint = payPoint;
        this.type = type;
        this.meta = new MongMetaEntity();
        this.state = new MongStateEntity();
        this.status = new MongStatusEntity(type.getMaxStatus());
    }

    /**
     * 0 단계 여부 확인
     * @return 0 단계 여부
     */
    public Boolean isEgg() {
        return Optional.ofNullable(this.type.getLevel()).orElse(0).equals(0);
    }

    /**
     * 죽음 여부 확인
     * @return 죽음 여부
     */
    public Boolean isDead() {
        return MongStateCode.DEAD.equals(this.state.getCode());
    }

    /**
     * 졸업 대기 상태 여부 확인
     * @return 졸업 대기 상태 여부
     */
    public Boolean isGraduateReady() {
        return MongStateCode.GRADUATE_READY.equals(this.state.getCode());
    }

    /**
     * 진화 대기 상태 여부 확인
     * @return 진화 대기 상태 여부
     */
    public Boolean isEvolutionReady() {
        return MongStateCode.EVOLUTION_READY.equals(this.state.getCode());
    }

    /**
     * 삭제
     */
    public void delete() {

        this.state.setCode(MongStateCode.DELETE);
        this.meta.deActivate();

        this.addHistory(MongHistoryEntity.MongHistoryType.DELETE);
    }

    /**
     * 쓰다 듬기
     * @param exp 쓰다 듬기 경험치
     */
    public void stroke(Double exp) {

        this.status.increaseExp(exp);
        this.meta.increaseStrokeCount();

        this.addHistory(MongHistoryEntity.MongHistoryType.STROKE);
    }

    /**
     * 수면
     */
    public void sleep() {

        this.state.setSleep();

        this.addHistory(MongHistoryEntity.MongHistoryType.SLEEP);
    }

    /**
     * 기상
     */
    public void wakeup() {

        this.state.setWakeup();

        this.addHistory(MongHistoryEntity.MongHistoryType.WAKEUP);
    }

    /**
     * 몽 배변 처리
     */
    public void poopClean(Double exp) {

        this.status.increaseExp(this.status.getPoopCount() * exp);
        this.status.resetPoopCount();

        this.addHistory(MongHistoryEntity.MongHistoryType.POOP_CLEAN);
    }

    /**
     * 몽 먹이 주기
     * @param increaseMongStatusDto 음식 종류에 따른 스텟 변경 수치 DTO
     */
    public void feed(Integer foodPrice, IncreaseMongStatusDto increaseMongStatusDto) {

        this.status.increaseWeight(increaseMongStatusDto.getWeight());
        this.status.increaseStatus(
                increaseMongStatusDto.getStrength(),
                increaseMongStatusDto.getSatiety(),
                increaseMongStatusDto.getHealthy(),
                increaseMongStatusDto.getFatigue()
        );

        this.payPoint = Math.max(0, this.payPoint - foodPrice);

        this.addHistory(MongHistoryEntity.MongHistoryType.FEED);
    }

    /**
     * 몽 훈련
     * @param patchMongStatusDto 지수 변동 값
     */
    public void training(PatchMongStatusDto patchMongStatusDto) {

        this.payPoint = Math.max(0, this.payPoint + patchMongStatusDto.getPayPoint());
        this.status.patchStatus(
                patchMongStatusDto.getWeight(),
                patchMongStatusDto.getPoopCount(),
                patchMongStatusDto.getExp(),
                patchMongStatusDto.getStrength(),
                patchMongStatusDto.getSatiety(),
                patchMongStatusDto.getHealthy(),
                patchMongStatusDto.getFatigue()
        );
        this.meta.increaseTrainingCount();

        this.addHistory(MongHistoryEntity.MongHistoryType.TRAINING);
    }

    /**
     * 몽 진화 준비
     */
    public void evolutionReady() {

        this.state.setCode(MongStateCode.EVOLUTION_READY);

        this.addHistory(MongHistoryEntity.MongHistoryType.EVOLUTION_READY);
    }

    /**
     * 몽 진화
     * @param nextMongType 다음 몽 타입
     */
    public void evolution(MongTypeEntity nextMongType, Double reward) {

        this.state.setCode(MongStateCode.NORMAL);
        this.status.resetExp();
        this.status.setMaxStatus(nextMongType.getMaxStatus());
        this.meta.setReward(reward);
        this.meta.resetStrokeCount();
        this.meta.resetTrainingCount();
        this.meta.resetPenalty();
        this.type = nextMongType;

        this.addHistory(MongHistoryEntity.MongHistoryType.EVOLUTION);
    }

    /**
     * 몽 졸업 준비
     */
    public void graduateReady() {

        this.state.setCode(MongStateCode.GRADUATE_READY);
        this.state.setWakeup();
        this.status.setCode(MongStatusCode.NORMAL);
        this.status.resetExp();

        this.addHistory(MongHistoryEntity.MongHistoryType.GRADUATE_READY);
    }

    /**
     * 몽 졸업
     */
    public void graduate() {

        this.state.setCode(MongStateCode.DELETE);
        this.status.setCode(MongStatusCode.NORMAL);
        this.meta.deActivate();

        this.addHistory(MongHistoryEntity.MongHistoryType.GRADUATE);
    }

    /**
     * 몽 사망
     */
    public void dead() {

        this.state.setCode(MongStateCode.DEAD);

        this.addHistory(MongHistoryEntity.MongHistoryType.DEAD);
    }

    /**
     * 몽 지수 증가
     * @param increaseMongStatusRatioDto 지수 증가치
     */
    public void increaseStatus(IncreaseMongStatusRatioDto increaseMongStatusRatioDto) {

        this.status.increaseExp(increaseMongStatusRatioDto.getExp());
        this.status.increaseWeight(increaseMongStatusRatioDto.getWeight());
        this.status.increaseStatusRatio(
                increaseMongStatusRatioDto.getStrengthRatio(),
                increaseMongStatusRatioDto.getSatietyRatio(),
                increaseMongStatusRatioDto.getHealthyRatio(),
                increaseMongStatusRatioDto.getFatigueRatio()
        );

        if (increaseMongStatusRatioDto.getPoopCount() > 0 && this.status.getPoopCount() >= MongStatusEntity.MAX_POOP_COUNT) {
            this.meta.increasePenalty();
        } else {
            this.status.increasePoopCount(increaseMongStatusRatioDto.getPoopCount());
        }
    }

    /**
     * 몽 지수 감소
     * @param decreaseMongStatusRatioDto 지수 감소치
     */
    public void decreaseStatus(DecreaseMongStatusRatioDto decreaseMongStatusRatioDto) {

        this.status.decreaseExp(decreaseMongStatusRatioDto.getExp());
        this.status.decreaseWeight(decreaseMongStatusRatioDto.getWeight());
        this.status.decreasePoopCount(decreaseMongStatusRatioDto.getPoopCount());
        this.status.decreaseStatusRatio(
                decreaseMongStatusRatioDto.getStrengthRatio(),
                decreaseMongStatusRatioDto.getSatietyRatio(),
                decreaseMongStatusRatioDto.getHealthyRatio(),
                decreaseMongStatusRatioDto.getFatigueRatio()
        );
    }

    /**
     * 몽 배변 증가
     * @param poopCount 배변 수
     */
    public void increasePoop(Integer poopCount) {

        if (poopCount > 0 && this.status.getPoopCount() >= MongStatusEntity.MAX_POOP_COUNT) {
            this.meta.increasePenalty();
        } else {
            this.status.increasePoopCount(poopCount);
        }
    }

    /**
     * 페이 포인트 증가
     * @param payPoint 증가할 페이 포인트
     */
    public void increasePayPoint(Integer payPoint) {

        if (payPoint == Integer.MAX_VALUE) return;

        this.payPoint = Math.min(this.payPoint + payPoint, Integer.MAX_VALUE);

        this.addHistory(MongHistoryEntity.MongHistoryType.INCREASE_PAY_POINT);
    }

    private void addHistory(MongHistoryEntity.MongHistoryType mongHistoryType) {

        this.history.add(MongHistoryEntity.builder()
                .mongHistoryType(mongHistoryType)
                .payPoint(this.payPoint)
                .typeCode(this.type.getComn().getCode())
                .build());
    }
}
