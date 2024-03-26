package com.mongs.auth.client;

import com.mongs.auth.client.dto.response.RegisterMemberResDto;
import com.mongs.core.interceptor.AdminFeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MEMBER", configuration = AdminFeignInterceptor.class)
public interface MemberClient {
    @PostMapping("/member/admin/{accountId}")
    ResponseEntity<RegisterMemberResDto> registerMember(@PathVariable("accountId") Long accountId);
}
