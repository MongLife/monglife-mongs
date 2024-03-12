package com.mongs.management.domain.mong.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "LIFECYCLE")
public interface LifecycleClient {
    @PutMapping("/lifecycle/sleep/{mongId}")
    ResponseEntity<Object> sleepMongEvent(@PathVariable("mongId") Long mongId);
    @PutMapping("/lifecycle/wakeup/{mongId}")
    ResponseEntity<Object> wakeupMongEvent(@PathVariable("mongId") Long mongId);
    @DeleteMapping("/lifecycle/evolution/{mongId}")
    ResponseEntity<Object> evolutionReadyMongEvent(@PathVariable("mongId") Long mongId);
    @PutMapping("/lifecycle/evolution/{mongId}")
    ResponseEntity<Object> evolutionMongEvent(@PathVariable("mongId") Long mongId);
    @DeleteMapping("/lifecycle/graduation/{mongId}")
    ResponseEntity<Object> graduationMongEvent(@PathVariable("mongId") Long mongId);
}
