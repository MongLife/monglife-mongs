package com.monglife.mongs.app.battle.dto.etc;

import com.monglife.mongs.app.battle.domain.MatchingEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class FindMatchingDto {

    private Long mongId;

    private String deviceId;

    private Long accountId;

    private Boolean isBot;

    @Builder
    public FindMatchingDto(Long mongId, String deviceId, Long accountId, Boolean isBot) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.isBot = isBot;
    }

    public static FindMatchingDto of(MatchingEntity matchingEntity) {
        return FindMatchingDto.builder()
                .mongId(matchingEntity.getMongId())
                .deviceId(matchingEntity.getDeviceId())
                .accountId(matchingEntity.getAccountId())
                .isBot(matchingEntity.getIsBot())
                .build();
    }
}
