package com.monglife.mongs.app.management.domain;

import com.monglife.mongs.app.management.global.enums.MongGrade;
import com.monglife.mongs.app.management.global.enums.MongShift;
import com.monglife.mongs.app.management.global.enums.MongState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Mong extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_id")
    private Long mongId;

    @Column(updatable = false, nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sleepTime;

    @Column(nullable = false)
    private String wakeUpTime;

    @Builder.Default
    @Column(nullable = false)
    private String mongCode = "CD444";

    @Builder.Default
    @Column(nullable = false)
    private Integer payPoint = 0;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MongGrade grade = MongGrade.EMPTY;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MongShift shift = MongShift.EMPTY;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MongState state = MongState.EMPTY;

    @Builder.Default
    @Column(nullable = false)
    private Double exp = 0D;

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
    private Integer poopCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isSleeping = false;

    @Builder.Default
    @Column(nullable = false)
    private Integer trainingCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer strokeCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer evolutionPoint = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer penalty = 0;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDeadSchedule = false;
}
