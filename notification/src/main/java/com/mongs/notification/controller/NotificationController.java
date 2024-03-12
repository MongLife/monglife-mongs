package com.mongs.notification.controller;

import com.mongs.core.mqtt.*;
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

    @PostMapping("/create")
    public void publishCreate(@RequestBody MqttReqDto<PublishCreate> request) throws Exception {
        notificationService.publishCreate(request.email(), request.data());
    }
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
