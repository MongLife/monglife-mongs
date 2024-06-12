package com.mongs.play.config;

import com.mongs.play.client.publisher.battle.service.MqttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(-999)
@Component
@RequiredArgsConstructor
public class StartupEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private final MqttService mqttService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("#######################     시작 준비 (Task 재시작)     #######################");
        log.info("####################### 시작 준비 완료 (Task 재시작 완료) #######################");
    }
}
