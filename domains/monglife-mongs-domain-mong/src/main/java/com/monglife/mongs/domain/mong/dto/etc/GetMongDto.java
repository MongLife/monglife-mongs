package com.monglife.mongs.domain.mong.dto.etc;

import com.monglife.mongs.domain.mong.entity.MongEntity;
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

    private Boolean isSleep;

    private LocalTime sleepAt;

    private LocalTime wakeupAt;


    public static GetMongDto of(MongEntity mongEntity) {
        return GetMongDto.builder()
                .mongId(mongEntity.getMongId())
                .mongName(mongEntity.getMongName())
                .mongCode(mongEntity.getType().getMongCode().getComnCode())
                .level(mongEntity.getType().getLevel())
                .isSleep(mongEntity.getState().getIsSleep())
                .sleepAt(mongEntity.getSleepAt())
                .wakeupAt(mongEntity.getWakeupAt())
                .build();
    }
}
