package com.mongs.management.domain.mong.client;

import com.mongs.core.interceptor.AccountFeignInterceptor;
import com.mongs.management.domain.mong.client.dto.response.*;
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
    @DeleteMapping("/lifecycle/evolution/{mongId}")
    ResponseEntity<EvolutionReadyMongEventResDto> evolutionReadyMongEvent(@PathVariable("mongId") Long mongId);
    @PutMapping("/lifecycle/evolution/{mongId}")
    ResponseEntity<EvolutionMongEventResDto> evolutionMongEvent(@PathVariable("mongId") Long mongId);
    @DeleteMapping("/lifecycle/graduation/{mongId}")
    ResponseEntity<GraduationMongEventResDto> graduationMongEvent(@PathVariable("mongId") Long mongId);
    @DeleteMapping("/lifecycle/delete/{mongId}")
    ResponseEntity<DeleteMongEventResDto> deleteMongEvent(@PathVariable("mongId") Long mongId);
}
