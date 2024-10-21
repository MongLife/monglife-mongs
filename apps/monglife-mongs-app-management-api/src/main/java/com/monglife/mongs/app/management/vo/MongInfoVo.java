package com.monglife.mongs.app.management.vo;

import com.monglife.mongs.app.management.domain.MongEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MongInfoVo {

    private Long mongId;

    private Long accountId;

    private String name;

    private String sleepTime;

    private String wakeUpTime;

    private String mongCode;

    private Integer payPoint;

    private Integer trainingCount;

    private Integer strokeCount;

    private Integer evolutionPoint;

    private Integer penalty;

    private Boolean isDeadSchedule;

    public static MongInfoVo of (MongEntity mongEntity) {
        return MongInfoVo.builder()
                .mongId(mongEntity.getMongId())
                .accountId(mongEntity.getAccountId())
                .name(mongEntity.getName())
                .sleepTime(mongEntity.getSleepTime())
                .wakeUpTime(mongEntity.getWakeUpTime())
                .mongCode(mongEntity.getMongCode())
                .payPoint(mongEntity.getPayPoint())
                .trainingCount(mongEntity.getTrainingCount())
                .strokeCount(mongEntity.getStrokeCount())
                .evolutionPoint(mongEntity.getEvolutionPoint())
                .penalty(mongEntity.getPenalty())
                .isDeadSchedule(mongEntity.getIsDeadSchedule())
                .build();
    }
}
