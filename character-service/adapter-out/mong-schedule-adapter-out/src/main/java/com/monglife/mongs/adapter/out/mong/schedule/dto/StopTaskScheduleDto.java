package com.monglife.mongs.adapter.out.mong.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StopTaskScheduleDto {

    private Long taskId;

    @Builder
    public StopTaskScheduleDto(Long taskId) {
        this.taskId = taskId;
    }
}
