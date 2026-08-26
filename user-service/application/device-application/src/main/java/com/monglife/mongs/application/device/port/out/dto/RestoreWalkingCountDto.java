package com.monglife.mongs.application.device.port.out.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 기기에 보낼 걸음 수 복구 알림
 *
 * @param eventId 같은 복구를 두 번 적립하지 않도록 기기가 중복을 걸러 내는 키
 */
@Getter
@ToString
public class RestoreWalkingCountDto {

    private final String deviceId;

    private final Integer restoreWalkingCount;

    private final String eventId;

    @Builder
    public RestoreWalkingCountDto(String deviceId, Integer restoreWalkingCount, String eventId) {
        this.deviceId = deviceId;
        this.restoreWalkingCount = restoreWalkingCount;
        this.eventId = eventId;
    }
}
