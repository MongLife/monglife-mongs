package com.mongs.auth.client;

import com.mongs.core.passport.PassportVO;
import com.mongs.core.security.principal.PassportDetail;
import feign.Headers;
import jakarta.ws.rs.HeaderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MEMBER")
public interface MemberClient {
    @GetMapping("/member/{accountId}")
    ResponseEntity<FindMemberResDto> findMember(@PathVariable("accountId") Long accountId);
    @PostMapping("/member/{accountId}")
    ResponseEntity<RegisterMemberResDto> registerMember(@PathVariable("accountId") Long accountId);
}
