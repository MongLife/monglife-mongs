package com.monglife.mongs.client.manager.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.client.manager.dto.request.ChargePayPointRequestDto;
import com.monglife.mongs.module.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MONGS-MANAGER", configuration = FeignClientConfig.class)
public interface ManagementClient {

    @GetMapping("/api/manager/internal/management/healthCheck")
    String healthCheck();

    @PatchMapping("/api/internal/manager/management/payPoint")
    ResponseEntity<ResponseDto<?>> chargePayPoint(@RequestBody ChargePayPointRequestDto chargePayPointRequestDto);
}
