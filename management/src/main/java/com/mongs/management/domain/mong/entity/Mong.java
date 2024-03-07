package com.mongs.management.domain.mong.entity;

import com.mongs.management.config.BaseTimeEntity;
import com.mongs.management.domain.ateFood.AteFoodHistory;
import com.mongs.management.domain.mong.service.dto.MongStatus;
import com.mongs.core.code.enums.management.MongCollapse;
import com.mongs.core.code.enums.management.MongCondition;
import com.mongs.core.code.enums.management.MongGrade;
import com.mongs.core.code.enums.management.MongShift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Builder(toBuilder = true)
public class Mong extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private Double weight = 0D;
    @Builder.Default
    @Column(nullable = false)
    private Double strength = 100D;
    @Builder.Default
    @Column(nullable = false)
    private Double satiety = 100D;
    @Builder.Default
    @Column(nullable = false)
    private Double healthy = 100D;
    @Builder.Default
    @Column(nullable = false)
    private Double sleep = 100D;
    @Builder.Default
    @Column(nullable = false)
    private Integer penalty = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer numberOfTraining = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer numberOfStroke = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer numberOfPoop = 0;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isSleeping = false;

    private String sleepTime;

    private String wakeUpTime;

    @Builder.Default
    @Enumerated(EnumType.STRING) // 몽 단계 1, 2, 3을 위해 만들어놓은 필드
    private MongGrade grade = MongGrade.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MongShift shift = MongShift.EMPTY;

    // condition mysql 예약어
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MongCondition mongCondition = MongCondition.NORMAL;

    // 회의 결과로 나눈다고 했기 때문에, 우선적으로 작성, -> 추후 리팩토링시 변경
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MongCondition lifeCycle = MongCondition.NORMAL;

    @Builder.Default
    private int paypoint = 0;

    @Builder.Default
    private int exp = 0;

    @OneToMany(mappedBy = "mong")
    private List<AteFoodHistory> foodHistoryList;

    @Builder.Default
    private int evolutionPoint = 0;

    public void doStroke(int stroke) {
        this.numberOfStroke = stroke + 1;
    }

    // 반대로 토글
    public void changeSleepCondition(Boolean isSleeping) {
        this.isSleeping = !isSleeping;
    }

    public void setNumberOfPoop(int poopCount) {
        this.numberOfPoop = poopCount;
    }

    public void setNumberOfTraining(int trainingCount) {
        this.numberOfTraining += trainingCount;
    }

    public MongStatus mongPassedTime(boolean minusSleep, int value) {
        // minusSleep가 true일 경우에만 healthy 감소하되, 0 이하로 내려가지 않도록 처리
        if (minusSleep) {
            this.healthy = Math.max(0, this.healthy - value);
        }

        // satiety와 strength 감소하되, 0 이하로 내려가지 않도록 처리
        this.satiety = Math.max(0, this.satiety - value);
        this.strength = Math.max(0, this.strength - value);

        // MongCollapse 상태 결정
        MongCollapse collapse = null;
        if (this.healthy == 0) {
            collapse = MongCollapse.NO_STAMINA;
        } else if (this.satiety == 0) {
            collapse = MongCollapse.STARVING;
        }

        return MongStatus.builder()
                .mongId(this.id)
                .collapse(collapse) // collapse가 null일 경우에도 Lombok @Builder는 null 값을 처리할 수 있음.
                .build();
    }


    public void shiftToDeath() {
        this.shift = MongShift.DIE;
    }

    public void addPoop() {
        this.numberOfPoop += 1;
    }

    public void setGrade(String name) {
        this.grade = MongGrade.valueOf(name);
    }

    public void setSatiety(double satiety) {
        this.satiety = satiety;
    }

    public void setPoint(int paypoint) {
        this.paypoint = paypoint;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }
}
