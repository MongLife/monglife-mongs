package com.mongs.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MEMBER", url = "http://localhost:9002")
public interface MemberClient {
    @PostMapping("/member/{accountId}")
    ResponseEntity<Object> registerMember(@PathVariable("accountId") Long accountId);
}
