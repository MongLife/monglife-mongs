package com.monglife.mongs.domain.mong.entity.data;

import com.monglife.mongs.domain.mong.dto.etc.DecreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.UpdateMongStatusDto;
import com.monglife.mongs.domain.mong.entity.history.MongHistoryEntity;
import com.monglife.mongs.domain.mong.entity.type.MongTypeEntity;
import com.monglife.mongs.domain.mong.enums.MongHistoryCode;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.listener.MongEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class, MongEntityListener.class })
@Table(name = "mongs_mong")
@ToString(exclude = { "type", "state", "status" })
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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_meta_id")
    private MongMetaEntity meta;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_state_id")
    private MongStateEntity state;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_status_id")
    private MongStatusEntity status;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_id")
    private List<MongHistoryEntity> history;

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

    public Boolean isDead() {
        return MongStateCode.DEAD.equals(this.state.getCode());
    }

    public Boolean isGraduateReady() {
        return MongStateCode.GRADUATE_READY.equals(this.state.getCode());
    }

    public Boolean isEvolutionReady() {
        return MongStateCode.EVOLUTION_READY.equals(this.state.getCode());
    }

    /**
     * 삭제
     */
    public void delete() {
        this.state.setCode(MongStateCode.NORMAL);
        this.meta.deActivate();
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_DELETE)
                .build());
    }

    /**
     * 쓰다 듬기
     * @param addExp 쓰다 듬기 경험치
     */
    public void stroke(Double addExp) {
        this.status.increaseExp(addExp);
        this.meta.increaseStrokeCount();
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_STROKE)
                .build());
    }

    /**
     * 수면
     */
    public void sleep() {
        this.state.setSleep();
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_SLEEP)
                .build());
    }

    /**
     * 기상
     */
    public void wakeup() {
        this.state.setWakeup();
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_WAKEUP)
                .build());
    }

    /**
     * 몽 배변 처리
     */
    public void poopClean(Double poopCleanExp) {
        this.status.increaseExp(this.status.getPoopCount() * poopCleanExp);
        this.status.resetPoopCount();
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_POOP_CLEAN)
                .build());
    }

    /**
     * 몽 먹이 주기
     * @param updateMongStatusDto 음식 종류에 따른 스텟 변경 수치 DTO
     */
    public void feed(Integer foodPrice, UpdateMongStatusDto updateMongStatusDto) {
        this.status.increaseWeight(updateMongStatusDto.getChangeWeightValue());
        this.status.increaseStatus(
                updateMongStatusDto.getChangeStrengthValue(),
                updateMongStatusDto.getChangeSatietyValue(),
                updateMongStatusDto.getChangeHealthyValue(),
                updateMongStatusDto.getChangeFatigueValue());

        this.payPoint = Math.max(0, this.payPoint - foodPrice);

        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_FEED)
                .payPoint(-foodPrice)
                .build());
    }

    /**
     * 몽 진화 준비
     */
    public void evolutionReady() {
        this.state.setCode(MongStateCode.EVOLUTION_READY);
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_EVOLUTION_READY)
                .build());
    }

    /**
     * 몽 진화
     * @param nextType 다음 몽 타입
     */
    public void evolution(MongTypeEntity nextType, Double reward) {
        this.state.setCode(MongStateCode.NORMAL);
        this.status.resetExp();
        this.status.setMaxStatus(nextType.getMaxStatus());
        this.meta.setReward(reward);
        this.meta.resetPenalty();
        this.type = nextType;
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_EVOLUTION)
                .typeCode(nextType.getComn().getCode())
                .build());
    }

    /**
     * 몽 졸업 준비
     */
    public void graduateReady() {
        this.state.setCode(MongStateCode.GRADUATE_READY);
        this.state.setWakeup();
        this.status.setCode(MongStatusCode.NORMAL);
        this.status.resetExp();
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_GRADUATE_READY)
                .build());
    }

    /**
     * 몽 졸업
     */
    public void graduate() {
        this.state.setCode(MongStateCode.NORMAL);
        this.status.setCode(MongStatusCode.NORMAL);
        this.meta.deActivate();
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_GRADUATE)
                .build());
    }

    /**
     * 몽 사망
     */
    public void dead() {
        this.state.setCode(MongStateCode.DEAD);
        this.history.add(MongHistoryEntity.builder()
                .mongHistoryCode(MongHistoryCode.HISTORY_MONG_DEAD)
                .build());
    }

    /**
     * 몽 지수 증가
     * @param increaseMongStatusDto 지수 증가치
     */
    public void increaseStatus(IncreaseMongStatusDto increaseMongStatusDto) {
        this.status.increaseExp(increaseMongStatusDto.getExp());
        this.status.increaseWeight(increaseMongStatusDto.getWeight());
        this.status.increasePoopCount(increaseMongStatusDto.getPoop());
        this.status.increaseStatusRatio(
                increaseMongStatusDto.getStrengthRatio(),
                increaseMongStatusDto.getSatietyRatio(),
                increaseMongStatusDto.getHealthyRatio(),
                increaseMongStatusDto.getFatigueRatio());
    }

    /**
     * 몽 지수 감소
     * @param decreaseMongStatusDto 지수 감소치
     */
    public void decreaseStatus(DecreaseMongStatusDto decreaseMongStatusDto) {
        this.status.decreaseExp(decreaseMongStatusDto.getExp());
        this.status.decreaseWeight(decreaseMongStatusDto.getWeight());
        this.status.decreasePoopCount(decreaseMongStatusDto.getPoop());
        this.status.decreaseStatusRatio(
                decreaseMongStatusDto.getStrengthRatio(),
                decreaseMongStatusDto.getSatietyRatio(),
                decreaseMongStatusDto.getHealthyRatio(),
                decreaseMongStatusDto.getFatigueRatio());
    }

    /**
     * 몽 배변 증가
     * @param addPoopCount 배변 수
     */
    public void increasePoop(Integer addPoopCount) {
        this.status.increasePoopCount(addPoopCount);
        if (this.status.getPoopCount() >= MongStatusEntity.MAX_POOP_COUNT) {
            this.meta.increasePenalty();
        }
    }

    public void increasePayPoint(Integer addPayPoint) {
        this.payPoint = Math.min(this.payPoint + addPayPoint, Integer.MAX_VALUE);
    }
}
