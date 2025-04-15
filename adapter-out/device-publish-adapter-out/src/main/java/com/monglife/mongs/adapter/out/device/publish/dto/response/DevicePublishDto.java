package com.monglife.mongs.adapter.out.device.publish.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DevicePublishDto {

    private String deviceId;

    private Integer walkingCount;

    private Integer consumeWalkingCount;

    @Builder
    public DevicePublishDto(String deviceId, Integer walkingCount, Integer consumeWalkingCount) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
    }
}
