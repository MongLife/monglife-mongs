package com.mongs.notification.controller;

import com.mongs.core.vo.mqtt.*;
import com.mongs.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification/admin")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/create")
    public void publishCreate(@RequestBody MqttReqDto<PublishCreateVo> request) throws Exception {
        log.info("{}", request.data());
        notificationService.publishCreate(request.accountId(), request.data());
    }
    @PostMapping("/status")
    public void publishStatus(@RequestBody MqttReqDto<PublishStatusVo> request) throws Exception {
        log.info("{}", request.data());
        notificationService.publishStatus(request.accountId(), request.data());
    }
    @PostMapping("/shift")
    public void publishShift(@RequestBody MqttReqDto<PublishShiftVo> request) throws Exception {
        log.info("{}", request.data());
        notificationService.publishShift(request.accountId(), request.data());
    }
    @PostMapping("/state")
    public void publishState(@RequestBody MqttReqDto<PublishStateVo> request) throws Exception {
        log.info("{}", request.data());
        notificationService.publishState(request.accountId(), request.data());
    }
}
