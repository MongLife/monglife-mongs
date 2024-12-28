package com.monglife.mongs.client.user.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.client.user.dto.request.CreateCollectionMapRequestDto;
import com.monglife.mongs.client.user.dto.request.CreateCollectionMongRequestDto;
import com.monglife.mongs.module.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MONGS-USER"/*, url="http://localhost:8040"*/, configuration = FeignClientConfig.class)
public interface CollectionClient {

    @PatchMapping("/user/internal/collection/health")
    String healthCheck();

    @PostMapping("/user/internal/collection/map")
    ResponseEntity<ResponseDto<?>> createCollectionMap(@RequestBody CreateCollectionMapRequestDto createCollectionMapRequestDto);

    @PostMapping("/user/internal/collection/mong")
    ResponseEntity<ResponseDto<?>> createCollectionMong(@RequestBody CreateCollectionMongRequestDto createCollectionMongRequestDto);
}
