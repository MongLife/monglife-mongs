package com.mongs.management.domain.mong.entity;

import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.core.entity.BaseTimeEntity;
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
    @Column(name = "mong_id")
    private Long id;
    @Column(updatable = false, nullable = false)
    private Long accountId;
    @Column(nullable = false)
    private String name;
    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(nullable = false)
    private String mongCode = "CD444";
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
    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // 몽 단계 1, 2, 3을 위해 만들어놓은 필드
    private MongGrade grade = MongGrade.EMPTY;
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MongShift shift = MongShift.NORMAL;
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MongState state = MongState.NORMAL;
    @Builder.Default
    @Column(nullable = false)
    private Integer evolutionPoint = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer payPoint = 0;
    @Builder.Default
    @Column(nullable = false)
    private Integer exp = 0;
}
