package com.mongs.management.domain.mong.client;

import com.mongs.core.vo.mqtt.MqttReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION")
public interface NotificationClient {

    @PostMapping("/notification/create")
    void publishCreate(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/status")
    void publishStatus(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/shift")
    void publishEvolution(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/state")
    void publishState(@RequestBody MqttReqDto<Object> request);
}
