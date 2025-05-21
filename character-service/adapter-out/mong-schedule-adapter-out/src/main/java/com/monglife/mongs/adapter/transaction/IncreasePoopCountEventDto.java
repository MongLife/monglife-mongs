package com.monglife.mongs.adapter.transaction;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class IncreasePoopCountEventDto {

    private Long taskId;

    @Builder
    public IncreasePoopCountEventDto(Long taskId) {
        this.taskId = taskId;
    }
}
