package com.mongs.auth.client;

import com.mongs.auth.dto.response.RegisterMemberResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MEMBER", configuration = AdminFeignInterceptor.class)
public interface MemberClient {
    @PostMapping("/member/admin/{accountId}")
    ResponseEntity<RegisterMemberResDto> registerMember(@PathVariable("accountId") Long accountId);
}
