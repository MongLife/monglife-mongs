package com.mongs.management.domain.mong.client;

import com.mongs.core.interceptor.AdminFeignInterceptor;
import com.mongs.management.domain.mong.client.dto.request.RegisterMapCollectionReqDto;
import com.mongs.management.domain.mong.client.dto.request.RegisterMongCollectionReqDto;
import com.mongs.management.domain.mong.client.dto.response.RegisterMapCollectionResDto;
import com.mongs.management.domain.mong.client.dto.response.RegisterMongCollectionResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MEMBER", configuration = AdminFeignInterceptor.class)
public interface CollectionClient {
    @PostMapping("/collection/admin/map/{accountId}")
    ResponseEntity<RegisterMapCollectionResDto> registerMapCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody RegisterMapCollectionReqDto registerMapCollectionReqDto
    );
    @PostMapping("/collection/admin/mong/{accountId}")
    ResponseEntity<RegisterMongCollectionResDto> registerMongCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody RegisterMongCollectionReqDto registerMongCollectionReqDto
    );
}
