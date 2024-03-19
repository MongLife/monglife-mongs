package com.mongs.lifecycle.client;

import com.mongs.core.interceptor.AccountFeignInterceptor;
import com.mongs.core.interceptor.AdminFeignInterceptor;
import com.mongs.core.vo.mqtt.MqttReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION", configuration = AdminFeignInterceptor.class)
public interface NotificationClient {
    @PostMapping("/notification/poop")
    void publishPoop(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/delete")
    void publishDelete(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/evolutionReady")
    void publishEvolutionReady(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/sleep")
    void publishSleep(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/healthy")
    void publishHealthy(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/satiety")
    void publishSatiety(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/strength")
    void publishStrength(@RequestBody MqttReqDto<Object> request);
    @PostMapping("/notification/weight")
    void publishWeight(@RequestBody MqttReqDto<Object> request);
}
