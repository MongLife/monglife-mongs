package com.monglife.mongs.client.fcm.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.client.fcm.dto.response.GetDeviceResponseDto;
import com.monglife.mongs.module.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "MONGLIFE-AUTH", path = "/api/auth", configuration = FeignClientConfig.class)
public interface AuthClient {

    @GetMapping("/device/{accountId}")
    ResponseEntity<ResponseDto<List<GetDeviceResponseDto>>> getDevices(@PathVariable("accountId") Long accountId);
}
