package com.mongs.notification.controller;

import com.mongs.core.vo.mqtt.*;
import com.mongs.core.vo.mqtt.MqttReqDto;
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
    public void publishCreate(@RequestBody MqttReqDto<PublishCreateVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishCreate(request.accountId(), request.data());
    }
    @PostMapping("/delete")
    public void publishDelete(@RequestBody MqttReqDto<PublishDeleteVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishDelete(request.accountId(), request.data());

    }
    @PostMapping("/stroke")
    public void publishStroke(@RequestBody MqttReqDto<PublishStrokeVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishStroke(request.accountId(), request.data());
    }
    @PostMapping("/feed")
    public void publishFeed(@RequestBody MqttReqDto<PublishFeedVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishFeed(request.accountId(), request.data());
    }
    @PostMapping("/poop")
    public void publishPoop(@RequestBody MqttReqDto<PublishPoopVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishPoop(request.accountId(), request.data());
    }
    @PostMapping("/sleeping")
    public void publishSleeping(@RequestBody MqttReqDto<PublishSleepingVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishSleeping(request.accountId(), request.data());
    }
    @PostMapping("/training")
    public void publishTraining(@RequestBody MqttReqDto<PublishTrainingVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishTraining(request.accountId(), request.data());
    }
    @PostMapping("/graduation")
    public void publishGraduation(@RequestBody MqttReqDto<PublishGraduationVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishGraduation(request.accountId(), request.data());
    }
    @PostMapping("/evolution")
    public void publishEvolution(@RequestBody MqttReqDto<PublishEvolutionVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishEvolution(request.accountId(), request.data());
    }
    @PostMapping("/evolutionReady")
    public void publishEvolutionReady(@RequestBody MqttReqDto<PublishEvolutionReadyVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishEvolutionReady(request.accountId(), request.data());
    }
    @PostMapping("/sleep")
    public void publishSleep(@RequestBody MqttReqDto<PublishSleepVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishSleep(request.accountId(), request.data());
    }
    @PostMapping("/healthy")
    public void publishHealthy(@RequestBody MqttReqDto<PublishHealthyVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishHealthy(request.accountId(), request.data());
    }
    @PostMapping("/satiety")
    public void publishSatiety(@RequestBody MqttReqDto<PublishSatietyVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishSatiety(request.accountId(), request.data());
    }
    @PostMapping("/strength")
    public void publishStrength(@RequestBody MqttReqDto<PublishStrengthVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishStrength(request.accountId(), request.data());
    }
    @PostMapping("/weight")
    public void publishWeight(@RequestBody MqttReqDto<PublishWeightVo> request) {
        log.info("{}, {}", request.accountId(), request.data());
        notificationService.publishWeight(request.accountId(), request.data());
    }
}
