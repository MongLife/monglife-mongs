package com.monglife.mongs.app.management.vo;

import com.monglife.mongs.app.management.domain.MongEntity;
import com.monglife.mongs.app.management.global.enums.MongGrade;
import com.monglife.mongs.app.management.global.enums.MongShift;
import com.monglife.mongs.app.management.global.enums.MongState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MongStatusVo {

    private Long mongId;

    private Double exp;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double sleep;

    private MongGrade grade;

    private MongShift shift;

    private MongState state;

    private Integer poopCount;

    private Boolean isSleeping;

    public static MongStatusVo of (MongEntity mongEntity) {
        return MongStatusVo.builder()
                .mongId(mongEntity.getMongId())
                .exp(mongEntity.getExp())
                .weight(mongEntity.getWeight())
                .strength(mongEntity.getStrength())
                .satiety(mongEntity.getSatiety())
                .healthy(mongEntity.getHealthy())
                .sleep(mongEntity.getSleep())
                .grade(mongEntity.getGrade())
                .shift(mongEntity.getShift())
                .state(mongEntity.getState())
                .poopCount(mongEntity.getPoopCount())
                .isSleeping(mongEntity.getIsSleeping())
                .build();
    }
}
