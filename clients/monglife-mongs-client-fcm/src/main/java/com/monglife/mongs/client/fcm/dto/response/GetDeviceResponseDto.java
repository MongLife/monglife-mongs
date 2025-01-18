package com.monglife.mongs.client.fcm.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetDeviceResponseDto {

    private String deviceId;

    private String deviceName;

    private String fcmToken;

    @Builder
    public GetDeviceResponseDto(String deviceId, String deviceName, String fcmToken) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.fcmToken = fcmToken;
    }
}
