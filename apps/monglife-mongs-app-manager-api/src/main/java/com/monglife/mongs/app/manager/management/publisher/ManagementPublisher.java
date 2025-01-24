package com.monglife.mongs.app.manager.management.publisher;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.manager.management.dto.etc.MongBasicDto;
import com.monglife.mongs.app.manager.management.dto.etc.MongStateDto;
import com.monglife.mongs.app.manager.management.dto.etc.MongStatusDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.client.fcm.service.FcmService;
import com.monglife.mongs.module.mqtt.annotation.MqttPublish;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagementPublisher {

    private final FcmService fcmService;

    @MqttPublish("/management/{topic}")
    public MqttResponseEntity<ResponseDto<MongBasicDto>> mongBasicObservePublish(Long mongId, MongBasicDto mongBasicDto) {

        return MqttResponseEntity
                .body(ManagementResponse.APP_MANAGER_MANAGEMENT_OBSERVE_MONG_BASIC.toResponseDto(mongBasicDto))
                .topic(mongId.toString());
    }

    @MqttPublish("/management/{topic}")
    public MqttResponseEntity<ResponseDto<MongStateDto>> mongStateObservePublish(Long mongId, MongStateDto mongStateDto) {

        return MqttResponseEntity
                .body(ManagementResponse.APP_MANAGER_MANAGEMENT_OBSERVE_MONG_STATE.toResponseDto(mongStateDto))
                .topic(mongId.toString());
    }

    @MqttPublish("/management/{topic}")
    public MqttResponseEntity<ResponseDto<MongStatusDto>> mongStatusObservePublish(Long mongId, MongStatusDto mongStatusDto) {

        return MqttResponseEntity
                .body(ManagementResponse.APP_MANAGER_MANAGEMENT_OBSERVE_MONG_STATUS.toResponseDto(mongStatusDto))
                .topic(mongId.toString());
    }

    public void mongStateHistoryPublish(Long accountId, String title, String body) {
        fcmService.sendPush(accountId, title, body);
    }

    public void mongStatusHistoryPublish(Long accountId, String title, String body) {
        fcmService.sendPush(accountId, title, body);
    }
}
