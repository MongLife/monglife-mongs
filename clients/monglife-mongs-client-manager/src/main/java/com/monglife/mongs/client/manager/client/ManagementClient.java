package com.monglife.mongs.client.manager.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.client.manager.dto.request.ChargePayPointRequestDto;
import com.monglife.mongs.client.manager.dto.request.ConsumePayPointRequestDto;
import com.monglife.mongs.client.manager.dto.request.PatchMongRequestDto;
import com.monglife.mongs.client.manager.dto.response.GetMinimalMongResponseDto;
import com.monglife.mongs.module.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "MONGS-MANAGER", path = "/manager", configuration = FeignClientConfig.class)
public interface ManagementClient {

    @PatchMapping("/internal/management/payPoint/charge/{mongId}")
    ResponseEntity<ResponseDto<?>> chargePayPoint(@PathVariable("mongId") Long mongId, @RequestBody ChargePayPointRequestDto chargePayPointRequestDto);

    @PatchMapping("/internal/management/payPoint/consume/{mongId}")
    ResponseEntity<ResponseDto<?>> consumePayPoint(@PathVariable("mongId") Long mongId, @RequestBody ConsumePayPointRequestDto consumePayPointRequestDto);

    @PostMapping("/internal/management/training/{mongId}")
    ResponseEntity<ResponseDto<?>> patchMongAfterTraining(@PathVariable("mongId") Long mongId, @RequestBody PatchMongRequestDto patchMongRequestDto);

    @PostMapping("/internal/management/battle/{mongId}")
    ResponseEntity<ResponseDto<?>> patchMongAfterBattle(@PathVariable("mongId") Long mongId, @RequestBody PatchMongRequestDto patchMongRequestDto);

    @GetMapping("/internal/management/{mongId}")
    ResponseEntity<ResponseDto<GetMinimalMongResponseDto>> getMong(@PathVariable("mongId") Long mongId);
}
