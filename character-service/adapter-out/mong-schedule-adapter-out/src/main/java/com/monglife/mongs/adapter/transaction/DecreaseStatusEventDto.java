package com.monglife.mongs.adapter.transaction;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class DecreaseStatusEventDto {

    private Long taskId;

    @Builder
    public DecreaseStatusEventDto(Long taskId) {
        this.taskId = taskId;
    }
}
