package com.monglife.mongs.adapter.out.device.publish.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttPublish;
import com.monglife.module.mqtt.dto.MqttResponseEntity;
import com.monglife.mongs.adapter.out.device.publish.dto.response.DevicePublishDto;
import com.monglife.mongs.adapter.out.device.publish.enums.AdapterOutPublishDeviceResponse;
import org.springframework.stereotype.Component;

@Component
public class DevicePublishClient {

    /**
     * 기기 변동 사항 비동기 응답
     * @param devicePublishDto 기기 변동 정보 비동기 응답 Dto
     */
    @MqttPublish("/device/{topic}")
    public MqttResponseEntity<ResponseDto<DevicePublishDto>> publishDevice(DevicePublishDto devicePublishDto) {

        String topic = devicePublishDto.getDeviceId();

        return MqttResponseEntity
                .body(AdapterOutPublishDeviceResponse.DEVICE_PUBLISH_DEVICE.toResponseDto(devicePublishDto))
                .topic(topic);
    }
}
