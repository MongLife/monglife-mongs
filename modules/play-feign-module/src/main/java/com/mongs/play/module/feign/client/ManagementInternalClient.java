package com.mongs.play.module.feign.client;

import com.mongs.play.module.feign.dto.req.*;
import com.mongs.play.module.feign.dto.res.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MANAGEMENT-INTERNAL")
public interface ManagementInternalClient {
    @PutMapping("/internal/management/evolutionReady")
    ResponseEntity<EvolutionReadyResDto> evolutionReady(@RequestBody EvolutionReadyReqDto evolutionReadyReqDto);
    @PutMapping("/internal/management/decreaseStatus")
    ResponseEntity<DecreaseStatusResDto> decreaseStatus(@RequestBody DecreaseStatusReqDto decreaseStatusReqDto);
    @PutMapping("/internal/management/increaseStatus")
    ResponseEntity<IncreaseStatusResDto> increaseStatus(@RequestBody IncreaseStatusReqDto increaseStatusReqDto);
    @PutMapping("/internal/management/increasePoopCount")
    ResponseEntity<IncreasePoopCountResDto> increasePoopCount(@RequestBody IncreasePoopCountReqDto increasePoopCountReqDto);
    @PutMapping("/internal/management/dead")
    ResponseEntity<DeadResDto> dead(@RequestBody DeadReqDto deadReqDto);
    @PutMapping("/internal/management/increasePayPoint")
    ResponseEntity<IncreasePayPointResDto> increasePayPoint(@RequestBody IncreasePayPointReqDto increasePayPointReqDto);
}
