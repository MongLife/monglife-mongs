package com.monglife.mongs.domain.battle.vo;

import com.monglife.mongs.domain.battle.entity.MatchingEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindMatchingVo {

    private Long mongId;

    private String deviceId;

    private Long accountId;

    private Boolean isBot;

    @Builder
    public FindMatchingVo(Long mongId, String deviceId, Long accountId, Boolean isBot) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.isBot = isBot;
    }

    public static FindMatchingVo of(MatchingEntity matchingEntity) {
        return FindMatchingVo.builder()
                .mongId(matchingEntity.getMongId())
                .deviceId(matchingEntity.getDeviceId())
                .accountId(matchingEntity.getAccountId())
                .isBot(matchingEntity.getIsBot())
                .build();
    }
}
