package com.mongs.gateway.feignClient;

import com.mongs.gateway.dto.request.PassportReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth")
public interface AuthFeignClient {
    @PostMapping("/auth/passport")
    ResponseEntity<Object> passport(@RequestBody PassportReqDto passportReqDto);
}
