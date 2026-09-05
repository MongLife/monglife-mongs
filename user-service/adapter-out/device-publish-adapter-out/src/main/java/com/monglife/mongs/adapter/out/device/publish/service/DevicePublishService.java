package com.monglife.mongs.adapter.out.device.publish.service;

import com.monglife.mongs.adapter.out.device.publish.client.DevicePublishClient;
import com.monglife.mongs.adapter.out.device.publish.dto.response.DevicePublishDto;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.application.device.port.out.dto.RestoreWalkingCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevicePublishService implements DevicePublishPort {

    private final DevicePublishClient devicePublishClient;

    /**
     * 걸음 수 복구 알림 비동기 발행
     */
    @Override
    public void publishRestoreWalkingCountPort(RestoreWalkingCountDto restoreWalkingCountDto) {

        DevicePublishDto devicePublishDto = DevicePublishDto.builder()
                .deviceId(restoreWalkingCountDto.getDeviceId())
                .restoreWalkingCount(restoreWalkingCountDto.getRestoreWalkingCount())
                .eventId(restoreWalkingCountDto.getEventId())
                .build();

        devicePublishClient.publishDevice(devicePublishDto);
    }
}
