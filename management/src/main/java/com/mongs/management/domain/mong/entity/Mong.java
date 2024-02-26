package com.mongs.management.domain.mong.entity;

import com.mongs.core.code.MongCode;
import com.mongs.core.code.MongConditionCode;
import com.mongs.core.time.BaseTimeEntity;
import com.mongs.management.domain.ateFood.AteFoodHistory;
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
}
