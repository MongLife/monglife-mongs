package com.monglife.mongs.domain.mong.dto.etc;

import com.monglife.mongs.domain.mong.entity.data.MongEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMongDto {

    private Long mongId;

    private String mongName;

    private String mongTypeCode;

    private Integer level;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double fatigue;

    private MongStateCode stateCode;

    private MongStatusCode statusCode;

    private Boolean isSleep;

    private LocalTime sleepAt;

    private LocalTime wakeupAt;

    private Boolean isEgg;


    public static GetMongDto of(MongEntity mongEntity) {
        return GetMongDto.builder()
                .mongId(mongEntity.getMongId())
                .mongName(mongEntity.getMongName())
                .mongTypeCode(mongEntity.getType().getMongCode().getComnCode())
                .level(mongEntity.getType().getLevel())
                .weight(mongEntity.getStatus().getWeight())
                .strength(mongEntity.getStatus().getStrength())
                .satiety(mongEntity.getStatus().getSatiety())
                .healthy(mongEntity.getStatus().getHealthy())
                .fatigue(mongEntity.getStatus().getFatigue())
                .stateCode(mongEntity.getState().getCode())
                .statusCode(mongEntity.getStatus().getCode())
                .isSleep(mongEntity.getState().getIsSleep())
                .sleepAt(mongEntity.getSleepAt())
                .wakeupAt(mongEntity.getWakeupAt())
                .isEgg(mongEntity.isEgg())
                .build();
    }
}
