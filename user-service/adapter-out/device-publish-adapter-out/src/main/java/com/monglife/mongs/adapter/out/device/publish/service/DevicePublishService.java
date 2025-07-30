package com.monglife.mongs.adapter.out.device.publish.service;

import com.monglife.mongs.adapter.out.device.publish.client.DevicePublishClient;
import com.monglife.mongs.adapter.out.device.publish.dto.response.DevicePublishDto;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import com.monglife.mongs.domain.device.model.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevicePublishService implements DevicePublishPort {

    private final DevicePublishClient devicePublishClient;

    /**
     * 보유 걸음 수 비동기 응답
     * @param step 걸음 수 도메인 객체
     */
    @Override
    public void publishCurrentWalkingCountPort(Step step) {

        DevicePublishDto devicePublishDto = DevicePublishDto.builder()
                .deviceId(step.getDeviceId())
                .walkingCount(step.getWalkingCount())
                .consumeWalkingCount(step.getConsumeWalkingCount())
                .build();

        devicePublishClient.publishDevice(devicePublishDto);
    }
}
