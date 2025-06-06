package com.monglife.mongs.adapter.out.mong.schedule.dto;

import com.monglife.module.common.logging.dto.LogDto;
import com.monglife.module.common.logging.enums.LogType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InitializerLogDto extends LogDto {

    private Integer taskCount;

    private List<Long> taskIds;

    @Builder
    public InitializerLogDto(String traceId, Integer traceOffset, String entryMethod, String className, String method, LogType logType, List<Long> taskIds, Integer taskCount) {
        super(traceId, traceOffset, entryMethod, className, method, logType);
        this.taskIds = taskIds;
        this.taskCount = taskCount;
    }
}
