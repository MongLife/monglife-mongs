package com.monglife.mongs.domain.mong.dto.etc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monglife.mongs.domain.mong.entity.MongEntity;
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

    private String mongCode;

    private Integer level;

    private Double weight;

    private Double strengthRatio;

    private Double satietyRatio;

    private Double healthyRatio;

    private Double fatigueRatio;

    private MongStateCode stateCode;

    private MongStatusCode statusCode;

    private Boolean isSleep;

    @JsonIgnore
    private LocalTime sleepAt;

    @JsonIgnore
    private LocalTime wakeupAt;

    @JsonIgnore
    private Boolean isEgg;


    public static GetMongDto of(MongEntity mongEntity) {
        return GetMongDto.builder()
                .mongId(mongEntity.getMongId())
                .mongName(mongEntity.getMongName())
                .mongCode(mongEntity.getType().getMongCode().getComnCode())
                .level(mongEntity.getType().getLevel())
                .weight(mongEntity.getStatus().getWeight())
                .strengthRatio(mongEntity.getStatus().getStrengthRatio())
                .satietyRatio(mongEntity.getStatus().getSatietyRatio())
                .healthyRatio(mongEntity.getStatus().getHealthyRatio())
                .fatigueRatio(mongEntity.getStatus().getFatigueRatio())
                .stateCode(mongEntity.getState().getCode())
                .statusCode(mongEntity.getStatus().getCode())
                .isSleep(mongEntity.getState().getIsSleep())
                .sleepAt(mongEntity.getSleepAt())
                .wakeupAt(mongEntity.getWakeupAt())
                .isEgg(mongEntity.isEgg())
                .build();
    }
}
