package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.dto.etc.DecreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusDto;
import com.monglife.mongs.domain.mong.dto.etc.UpdateMongStatusDto;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.listener.MongEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
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
     * 삭제
     */
    public void delete() {

        this.state.update(MongStateEntity.UpdateDto.builder()
                .code(MongStateCode.NORMAL)
                .isSleep(Boolean.FALSE)
                .build());

        this.meta.update(MongMetaEntity.UpdateDto.builder()
                .isActive(Boolean.FALSE)
                .build());
    }

    /**
     * 쓰다 듬기
     * @param addExp 쓰다 듬기 경험치
     */
    public void stroke(Double addExp) {

        this.status.update(MongStatusEntity.UpdateDto.builder()
                .exp(this.status.getExp() + addExp)
                .build());

        this.meta.update(MongMetaEntity.UpdateDto.builder()
                .strokeCount(this.meta.getStrokeCount() + 1)
                .build());
    }

    /**
     * 수면
     */
    public void sleep() {

        this.state.update(MongStateEntity.UpdateDto.builder()
                .isSleep(Boolean.TRUE)
                .build());
    }

    /**
     * 기상
     */
    public void wakeup() {

        this.state.update(MongStateEntity.UpdateDto.builder()
                .isSleep(Boolean.FALSE)
                .build());
    }

    /**
     * 몽 배변 처리
     */
    public void poopClean(Double poopCleanExp) {

        Double nextExp = this.status.getExp() + (this.status.getPoopCount() * poopCleanExp);

        this.status.update(MongStatusEntity.UpdateDto.builder()
                .poopCount(0)
                .exp(nextExp)
                .build());
    }

    /**
     * 몽 먹이 주기
     * @param updateMongStatusDto 음식 종류에 따른 스텟 변경 수치 DTO
     */
    public void feed(Integer foodPrice, UpdateMongStatusDto updateMongStatusDto) {

        MongStatusEntity.UpdateDto updateDto = MongStatusEntity.UpdateDto.builder()
                .weight(this.status.getWeight() + updateMongStatusDto.getChangeWeightValue())
                .strength(this.status.getStrength() + updateMongStatusDto.getChangeStrengthValue())
                .satiety(this.status.getSatiety() + updateMongStatusDto.getChangeSatietyValue())
                .healthy(this.status.getHealthy() + updateMongStatusDto.getChangeHealthyValue())
                .fatigue(this.status.getFatigue() + updateMongStatusDto.getChangeFatigueValue())
                .build();

        this.status.update(updateDto);
        this.payPoint = Math.max(0, this.payPoint - foodPrice);
    }

    /**
     * 몽 진화 준비
     */
    public void evolutionReady() {

        this.state.update(MongStateEntity.UpdateDto.builder()
                .code(MongStateCode.EVOLUTION_READY)
                .build());
    }

    /**
     * 몽 진화
     * @param nextType 다음 몽 타입
     */
    public void evolution(MongTypeEntity nextType) {

        this.state.update(MongStateEntity.UpdateDto.builder()
                .code(MongStateCode.NORMAL)
                .build());

        this.status.update(MongStatusEntity.UpdateDto.builder()
                .exp(0D)
                .build());

        this.status.updateMaxStatus(nextType.getMaxStatus());
        this.type = nextType;
    }

    /**
     * 몽 졸업 준비
     */
    public void graduateReady() {

        this.status.update(MongStatusEntity.UpdateDto.builder()
                .code(MongStatusCode.NORMAL)
                .weight(this.type.getMaxStatus())
                .strength(this.type.getMaxStatus())
                .satiety(this.type.getMaxStatus())
                .healthy(this.type.getMaxStatus())
                .fatigue(this.type.getMaxStatus())
                .build());

        this.state.update(MongStateEntity.UpdateDto.builder()
                .code(MongStateCode.GRADUATE_READY)
                .isSleep(Boolean.FALSE)
                .build());
    }

    /**
     * 몽 졸업
     */
    public void graduate() {

        this.status.update(MongStatusEntity.UpdateDto.builder()
                .code(MongStatusCode.NORMAL)
                .build());

        this.state.update(MongStateEntity.UpdateDto.builder()
                .code(MongStateCode.NORMAL)
                .isSleep(Boolean.FALSE)
                .build());
    }

    /**
     * 몽 사망
     */
    public void dead() {

        this.status.update(MongStatusEntity.UpdateDto.builder()
                .code(MongStatusCode.NORMAL)
                .build());

        this.state.update(MongStateEntity.UpdateDto.builder()
                .code(MongStateCode.DEAD)
                .isSleep(Boolean.FALSE)
                .build());
    }

    /**
     * 몽 지수 증가
     * @param increaseMongStatusDto 지수 증가치
     */
    public void increaseStatus(IncreaseMongStatusDto increaseMongStatusDto) {
        this.status.updateRatio(MongStatusEntity.UpdateRatioDto.builder()
                .exp(increaseMongStatusDto.getExp())
                .weight(increaseMongStatusDto.getWeight())
                .strengthRatio(increaseMongStatusDto.getStrengthRatio())
                .satietyRatio(increaseMongStatusDto.getSatietyRatio())
                .healthyRatio(increaseMongStatusDto.getHealthyRatio())
                .fatigueRatio(increaseMongStatusDto.getFatigueRatio())
                .poopCount(increaseMongStatusDto.getPoop())
                .build());
    }

    /**
     * 몽 지수 감소
     * @param decreaseMongStatusDto 지수 감소치
     */
    public void decreaseStatus(DecreaseMongStatusDto decreaseMongStatusDto) {
        this.status.updateRatio(MongStatusEntity.UpdateRatioDto.builder()
                .exp(decreaseMongStatusDto.getExp())
                .weight(decreaseMongStatusDto.getWeight())
                .strengthRatio(decreaseMongStatusDto.getStrengthRatio())
                .satietyRatio(decreaseMongStatusDto.getSatietyRatio())
                .healthyRatio(decreaseMongStatusDto.getHealthyRatio())
                .fatigueRatio(decreaseMongStatusDto.getFatigueRatio())
                .poopCount(decreaseMongStatusDto.getPoop())
                .build());
    }

    /**
     * 몽 배변 증가
     * @param addPoopCount 배변 수
     */
    public void increasePoop(Integer addPoopCount) {

        int prePoopCount = this.status.getPoopCount();

        this.status.update(MongStatusEntity.UpdateDto.builder()
                .poopCount(this.status.getPoopCount() + addPoopCount)
                .build());

        if (this.status.getPoopCount() == prePoopCount) {
            this.meta.update(MongMetaEntity.UpdateDto.builder()
                    .penalty(this.meta.getPenalty() + 0.1)
                    .build());
        }
    }
}
