package com.monglife.mongs.adapter.out.device.publish.dto.response;

import lombok.*;

/**
 * 기기에 보내는 걸음 수 복구 알림
 *
 * 서버는 걸음 수 잔액을 보관하지 않으므로 "현재 잔액"을 알려 줄 수 없다.
 * 대신 환전이 실패했으니 이만큼 되돌리라는 지시를 보낸다.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DevicePublishDto {

    private String deviceId;

    private Integer restoreWalkingCount;

    /**
     * 기기가 같은 복구를 두 번 적립하지 않도록 거르는 키
     */
    private String eventId;

    @Builder
    public DevicePublishDto(String deviceId, Integer restoreWalkingCount, String eventId) {
        this.deviceId = deviceId;
        this.restoreWalkingCount = restoreWalkingCount;
        this.eventId = eventId;
    }
}
