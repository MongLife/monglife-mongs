package com.monglife.mongs.client.user.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.client.user.dto.request.CreateCollectionMongRequestDto;
import com.monglife.mongs.client.user.dto.response.GetCollectionMongResponseDto;
import com.monglife.mongs.module.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "MONGS-USER", path = "/user", configuration = FeignClientConfig.class)
public interface CollectionClient {

    @PostMapping("/internal/collection/mong")
    ResponseEntity<ResponseDto<?>> createCollectionMong(@RequestBody CreateCollectionMongRequestDto createCollectionMongRequestDto);

    @GetMapping("/collection/mong")
    ResponseEntity<ResponseDto<List<GetCollectionMongResponseDto>>> getCollectionMongs();
}
