package com.mongs.management.domain.mong.client;

import com.mongs.core.interceptor.AdminFeignInterceptor;
import com.mongs.management.domain.mong.client.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "LIFECYCLE", configuration = AdminFeignInterceptor.class)
public interface LifecycleClient {
    @PostMapping("/lifecycle/admin/egg/{mongId}")
    ResponseEntity<EggMongEventResDto> eggMongEvent(@PathVariable("mongId") Long mongId);
    @PutMapping("/lifecycle/admin/sleep/{mongId}")
    ResponseEntity<SleepMongEventResDto> sleepMongEvent(@PathVariable("mongId") Long mongId);
    @PutMapping("/lifecycle/admin/wakeup/{mongId}")
    ResponseEntity<WakeupMongEventResDto> wakeupMongEvent(@PathVariable("mongId") Long mongId);
    @DeleteMapping("/lifecycle/admin/evolution/{mongId}")
    ResponseEntity<EvolutionReadyMongEventResDto> evolutionReadyMongEvent(@PathVariable("mongId") Long mongId);
    @PutMapping("/lifecycle/admin/evolution/{mongId}")
    ResponseEntity<EvolutionMongEventResDto> evolutionMongEvent(@PathVariable("mongId") Long mongId);
    @DeleteMapping("/lifecycle/admin/graduation/{mongId}")
    ResponseEntity<GraduationMongEventResDto> graduationMongEvent(@PathVariable("mongId") Long mongId);
    @DeleteMapping("/dead/{mongId}")
    ResponseEntity<DeadMongEventResDto> deadMongEvent(@PathVariable("mongId") Long mongId);
}
