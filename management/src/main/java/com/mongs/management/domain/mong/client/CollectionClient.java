package com.mongs.management.domain.mong.client;

import com.mongs.core.interceptor.AccountFeignInterceptor;
import com.mongs.management.domain.mong.client.dto.request.RegisterMapCollectionReqDto;
import com.mongs.management.domain.mong.client.dto.request.RegisterMongCollectionReqDto;
import com.mongs.management.domain.mong.client.dto.response.RegisterMapCollectionResDto;
import com.mongs.management.domain.mong.client.dto.response.RegisterMongCollectionResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MEMBER", configuration = AccountFeignInterceptor.class)
public interface CollectionClient {
    @PostMapping("/collection/map")
    ResponseEntity<RegisterMapCollectionResDto> registerMapCollection(@RequestBody RegisterMapCollectionReqDto registerMapCollectionReqDto);
    @PostMapping("/collection/mong")
    ResponseEntity<RegisterMongCollectionResDto> registerMongCollection(@RequestBody RegisterMongCollectionReqDto registerMongCollectionReqDto);
}
