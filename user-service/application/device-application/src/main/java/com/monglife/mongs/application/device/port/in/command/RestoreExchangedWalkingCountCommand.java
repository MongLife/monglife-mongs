package com.monglife.mongs.application.device.port.in.command;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RestoreExchangedWalkingCountCommand {

    private final String deviceId;

    private final Integer walkingCount;

    /**
     * 기기가 같은 복구를 두 번 적립하지 않도록 거르는 키.
     * 롤백 이벤트의 transactionId 를 그대로 쓰므로, 같은 이벤트가 재전달되어도 값이 같다.
     */
    private final String eventId;

    @Builder
    public RestoreExchangedWalkingCountCommand(String deviceId, Integer walkingCount, String eventId) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.eventId = eventId;
    }
}
