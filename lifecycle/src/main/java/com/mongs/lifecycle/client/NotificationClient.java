package com.mongs.lifecycle.client;

import com.mongs.core.mqtt.MqttReqDto;
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
    void publishShift(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/state")
    void publishState(@RequestBody MqttReqDto<Object> request);
}
