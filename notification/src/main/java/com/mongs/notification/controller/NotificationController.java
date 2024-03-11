package com.mongs.notification.controller;

import com.mongs.core.mqtt.MqttReqDto;
import com.mongs.core.mqtt.PublishShift;
import com.mongs.core.mqtt.PublishState;
import com.mongs.core.mqtt.PublishStatus;
import com.mongs.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/status")
    public void publishStatus(@RequestBody MqttReqDto<PublishStatus> request) throws Exception {
        notificationService.publishStatus(request.email(), request.data());
    }
    @PostMapping("/shift")
    public void publishEvolution(@RequestBody MqttReqDto<PublishShift> request) throws Exception {
        notificationService.publishShift(request.email(), request.data());
    }
    @PostMapping("/state")
    public void publishState(@RequestBody MqttReqDto<PublishState> request) throws Exception {
        notificationService.publishState(request.email(), request.data());
    }
}
