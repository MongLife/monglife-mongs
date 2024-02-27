package com.mongs.management.domain.mong.entity;

import com.mongs.core.code.MongCode;
import com.mongs.core.code.MongConditionCode;
import com.mongs.core.time.BaseTimeEntity;
import com.mongs.management.domain.ateFood.AteFoodHistory;
import com.mongs.management.domain.mong.service.dto.MongStatus;
import com.mongs.management.domain.mong.service.enums.MongCollapse;
import jakarta.persistence.*;
import lombok.*;
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

    // 몽 이름
    private String name;

    private double weight;

    @Builder.Default
    private int strength = 100;

    @Builder.Default
    private int satiety = 100;

    @Builder.Default
    private int healthy = 100;

    private Boolean isSleeping;

    @Builder.Default
    private int penalty = 0;

    @Builder.Default
    private int numberOfTraining = 0;

    @Builder.Default
    private int numberOfStroke = 0;

    @Builder.Default
    private int numberOfPoop = 0;

    private String sleepTime;

    private String wakeUpTime;

    @Enumerated(EnumType.STRING) // 몽 단계 1, 2, 3을 위해 만들어놓은 필드
    private MongCode grade;

    @Enumerated(EnumType.STRING)
    private MongConditionCode state;

    @Enumerated(EnumType.STRING)
    private MongConditionCode lifeCycle;

    @Builder.Default
    private int paypoint = 0;

    @OneToMany(mappedBy = "mong")
    private List<AteFoodHistory> foodHistoryList;

    public void doStroke(int stroke) {
        this.numberOfStroke = stroke + 1;
    }

    public void changeSleepConditon (Boolean isSleeping) {
        this.isSleeping = isSleeping;
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


    public void stateToDeath() {
        this.state = MongConditionCode.DIE;
    }

    public void addPoop() {
        this.numberOfPoop += 1;
    }
}
