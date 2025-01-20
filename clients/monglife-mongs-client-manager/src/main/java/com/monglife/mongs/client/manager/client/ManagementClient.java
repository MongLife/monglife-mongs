package com.monglife.mongs.client.manager.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.client.manager.dto.request.ChargePayPointRequestDto;
import com.monglife.mongs.client.manager.dto.request.PatchMongAfterTrainingRequestDto;
import com.monglife.mongs.client.manager.dto.response.GetMinimalMongResponseDto;
import com.monglife.mongs.module.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MONGS-MANAGER", path = "/manager", configuration = FeignClientConfig.class)
public interface ManagementClient {

    @PostMapping("/internal/management/payPoint/{mongId}")
    ResponseEntity<ResponseDto<?>> chargePayPoint(@PathVariable("mongId") Long mongId, @RequestBody ChargePayPointRequestDto chargePayPointRequestDto);

    @PostMapping("/internal/management/{mongId}")
    ResponseEntity<ResponseDto<?>> patchMongAfterTraining(@PathVariable("mongId") Long mongId, @RequestBody PatchMongAfterTrainingRequestDto patchMongAfterTrainingRequestDto);

    @GetMapping("/open/management/{mongId}")
    ResponseEntity<ResponseDto<GetMinimalMongResponseDto>> getMong(@PathVariable("mongId") Long mongId);
}
