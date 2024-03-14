package com.mongs.lifecycle.client;

import com.mongs.core.vo.mqtt.MqttReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION", configuration = AdminFeignInterceptor.class)
public interface NotificationClient {

    @PostMapping("/notification/admin/create")
    void publishCreate(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/admin/status")
    void publishStatus(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/admin/shift")
    void publishShift(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/admin/state")
    void publishState(@RequestBody MqttReqDto<Object> request);
}
