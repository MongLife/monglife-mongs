package com.monglife.mongs.app.manager.management.publisher;

import com.monglife.core.dto.event.SendNotificationDto;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.module.mqtt.annotation.MqttPublish;
import com.monglife.module.mqtt.dto.MqttResponseEntity;
import com.monglife.mongs.app.manager.management.dto.etc.MongBasicDto;
import com.monglife.mongs.app.manager.management.dto.etc.MongStateDto;
import com.monglife.mongs.app.manager.management.dto.etc.MongStatusDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagementPublisher {

    private final KafkaService kafkaService;

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

    public void mongStateHistoryPublish(Long accountId, String mongName, MongStateCode stateCode) {

        String topic = "notification";
        String title = "";
        String body = "";

        switch (stateCode) {
            case DEAD -> {
                title = "죽은 몽이 있어요";
                body = mongName + "(이)가 죽었어요...";
            }

            case EVOLUTION_READY -> {
                title = "진화 준비가 되었어요";
                body = mongName + "(을)를 새로운 몽으로 진화시켜 주세요";
            }

            case GRADUATE_READY -> {
                title = "졸업 준비가 되었어요";
                body = mongName + "(을)를 졸업 시켜 주세요";
            }
        }


        if (!title.isBlank() && !body.isBlank()) {
            kafkaService.generateEvent(topic, SendNotificationDto.builder()
                    .accountId(accountId)
                    .title(title)
                    .body(body)
                    .isAppForegroundMessage(false)
                    .build());
        }
    }

    public void mongStatusHistoryPublish(Long accountId, String mongName, MongStatusCode statusCode) {

        String topic = "notification";
        String title = "";
        String body = "";

        switch (statusCode) {
            case SOMNOLENCE -> {
                title = "졸린 몽이 있어요";
                body = mongName + "(을)를 재워야 해요";
            }
            case HUNGRY -> {
                title = "배고픈 몽이 있어요";
                body = mongName + "에게 밥을 줘야 해요";

            }
            case SICK -> {
                title = "아픈 몽이 있어요";
                body = mongName + "의 체력을 채워야 해요";
            }
        }

        if (!title.isBlank() && !body.isBlank()) {
            kafkaService.generateEvent(topic, SendNotificationDto.builder()
                    .accountId(accountId)
                    .title(title)
                    .body(body)
                    .isAppForegroundMessage(false)
                    .build());
        }
    }
}
