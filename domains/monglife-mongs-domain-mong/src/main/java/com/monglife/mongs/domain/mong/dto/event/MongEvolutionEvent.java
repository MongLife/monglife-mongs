package com.monglife.mongs.domain.mong.dto.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MongEvolutionEvent {

    private Long mongId;

    @Builder
    public MongEvolutionEvent(Long mongId) {
        this.mongId = mongId;
    }
}
