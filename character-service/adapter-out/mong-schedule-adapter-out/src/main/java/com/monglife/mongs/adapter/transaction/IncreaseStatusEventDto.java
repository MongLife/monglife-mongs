package com.monglife.mongs.adapter.transaction;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class IncreaseStatusEventDto {

    private Long taskId;

    @Builder
    public IncreaseStatusEventDto(Long taskId) {
        this.taskId = taskId;
    }
}
