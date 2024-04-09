package com.mongs.management.domain.management.client;

import com.mongs.core.interceptor.AccountFeignInterceptor;
import com.mongs.management.domain.management.client.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "LIFECYCLE", configuration = AccountFeignInterceptor.class)
public interface LifecycleClient {
    @PostMapping("/lifecycle/egg/{mongId}")
    ResponseEntity<EggMongEventResDto> eggMongEvent(@PathVariable("mongId") Long mongId);
    @PutMapping("/lifecycle/sleep/{mongId}")
    ResponseEntity<SleepMongEventResDto> sleepMongEvent(@PathVariable("mongId") Long mongId);
    @PutMapping("/lifecycle/wakeup/{mongId}")
    ResponseEntity<WakeupMongEventResDto> wakeupMongEvent(@PathVariable("mongId") Long mongId);
    @PutMapping("/lifecycle/eggEvolution/{mongId}")
    ResponseEntity<EvolutionMongEventResDto> eggEvolutionMongEvent(@PathVariable("mongId") Long mongId);
    @DeleteMapping("/lifecycle/graduation/{mongId}")
    ResponseEntity<GraduationMongEventResDto> graduationReadyMongEvent(@PathVariable("mongId") Long mongId);
    @DeleteMapping("/lifecycle/delete/{mongId}")
    ResponseEntity<DeleteMongEventResDto> deleteMongEvent(@PathVariable("mongId") Long mongId);
}