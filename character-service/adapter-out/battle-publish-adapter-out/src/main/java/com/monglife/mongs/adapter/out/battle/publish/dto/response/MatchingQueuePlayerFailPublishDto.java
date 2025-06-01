package com.monglife.mongs.adapter.out.battle.publish.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MatchingQueuePlayerFailPublishDto {

    private String deviceId;

    @Builder
    public MatchingQueuePlayerFailPublishDto(String deviceId) {
        this.deviceId = deviceId;
    }
}
