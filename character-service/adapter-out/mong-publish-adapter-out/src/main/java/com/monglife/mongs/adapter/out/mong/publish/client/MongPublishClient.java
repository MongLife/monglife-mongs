package com.monglife.mongs.adapter.out.mong.publish.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttPublish;
import com.monglife.module.mqtt.dto.MqttResponseEntity;
import com.monglife.mongs.adapter.out.mong.publish.dto.response.MongPublishDto;
import com.monglife.mongs.adapter.out.mong.publish.enums.AdapterOutPublishMongResponse;
import org.springframework.stereotype.Component;

@Component
public class MongPublishClient {

    @MqttPublish("/management/{topic}")
    public MqttResponseEntity<ResponseDto<MongPublishDto>> mongBasicObservePublish(MongPublishDto mongPublishDto) {

        String topic = String.valueOf(mongPublishDto.getMongId());

        return MqttResponseEntity
                .body(AdapterOutPublishMongResponse.MONG_PUBLISH_MONG.toResponseDto(mongPublishDto))
                .topic(topic);
    }
}
