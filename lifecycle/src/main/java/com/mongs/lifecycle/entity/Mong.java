package com.mongs.lifecycle.entity;

import com.mongs.core.code.enums.management.MongGrade;
import com.mongs.core.code.enums.management.MongShift;
import com.mongs.core.code.enums.management.MongState;
import com.mongs.core.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Builder(toBuilder = true)
public class Mong extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(updatable = false, nullable = false)
    private Long accountId;
    @Column(nullable = false)
    private String name;
    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = false;

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
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MongGrade grade = MongGrade.ZERO;
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MongShift shift = MongShift.EMPTY;
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MongState state = MongState.EMPTY;

    @Builder.Default
    @Column(nullable = false)
    private Integer payPoint = 0;
//
//    @OneToMany(mappedBy = "mong")
//    private List<AteFoodHistory> foodHistoryList;
//
//    public void doStroke(int stroke) {
//        this.numberOfStroke = stroke + 1;
//    }
//
//    public void changeSleepCondition(Boolean isSleeping) {
//        this.isSleeping = isSleeping;
//    }
//
//    public void setNumberOfPoop(int poopCount) {
//        this.numberOfPoop = poopCount;
//    }
//
//    public void setNumberOfTraining(int trainingCount) {
//        this.numberOfTraining += trainingCount;
//    }
//
//    public MongStatus mongPassedTime(boolean minusSleep, int value) {
//        // minusSleep가 true일 경우에만 healthy 감소하되, 0 이하로 내려가지 않도록 처리
//        if (minusSleep) {
//            this.healthy = Math.max(0, this.healthy - value);
//        }
//
//        // satiety와 strength 감소하되, 0 이하로 내려가지 않도록 처리
//        this.satiety = Math.max(0, this.satiety - value);
//        this.strength = Math.max(0, this.strength - value);
//
//        // MongCollapse 상태 결정
//        MongCollapse collapse = null;
//        if (this.healthy == 0) {
//            collapse = MongCollapse.NO_STAMINA;
//        } else if (this.satiety == 0) {
//            collapse = MongCollapse.STARVING;
//        }
//
//        return MongStatus.builder()
//                .mongId(this.id)
//                .collapse(collapse) // collapse가 null일 경우에도 Lombok @Builder는 null 값을 처리할 수 있음.
//                .build();
//    }
//
//
//    public void shiftToDeath() {
//        this.shift = MongShift.DIE;
//    }
//
//    public void addPoop() {
//        this.numberOfPoop += 1;
//    }
}
