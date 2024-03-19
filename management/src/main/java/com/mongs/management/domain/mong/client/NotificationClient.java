package com.mongs.management.domain.mong.client;

import com.mongs.core.interceptor.AccountFeignInterceptor;
import com.mongs.core.interceptor.AdminFeignInterceptor;
import com.mongs.core.vo.mqtt.MqttReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION", configuration = AdminFeignInterceptor.class)
public interface NotificationClient {
    @PostMapping("/notification/create")
    void publishCreate(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/delete")
    void publishDelete(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/stroke")
    void publishStroke(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/feed")
    void publishFeed(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/sleep")
    void publishSleep(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/poop")
    void publishPoop(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/training")
    void publishTraining(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/graduation")
    void publishGraduation(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/evolution")
    void publishEvolution(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/evolutionReady")
    void publishEvolutionReady(@RequestBody MqttReqDto<Object> request);
}
