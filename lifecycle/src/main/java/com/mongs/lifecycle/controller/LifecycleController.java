package com.mongs.lifecycle.controller;

import com.mongs.lifecycle.dto.response.*;
import com.mongs.lifecycle.service.LifecycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lifecycle")
public class LifecycleController {

    private final LifecycleService lifecycleService;

    @GetMapping("/stress/{size}")
    public ResponseEntity<Object> stressTest(@PathVariable("size") Integer size) {

        for (long mongId = 1; mongId <= Math.min(1000, size); mongId++) {
            lifecycleService.stressTestEvent(mongId);
        }

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/evolution/{mongId}")
    public ResponseEntity<Object> evolutionMongEvent(@PathVariable("mongId") Long mongId) {
        lifecycleService.evolutionEvent(mongId);

        return ResponseEntity.ok().body(EvolutionMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<Object> sleepMongEvent(@PathVariable("mongId") Long mongId) {
        lifecycleService.sleepEvent(mongId);

        return ResponseEntity.ok().body(SleepMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PutMapping("/wakeup/{mongId}")
    public ResponseEntity<Object> wakeupMongEvent(@PathVariable("mongId") Long mongId) {
        lifecycleService.wakeupEvent(mongId);

        return ResponseEntity.ok().body(WakeupMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @DeleteMapping("/admin/dead/{mongId}")
    public ResponseEntity<Object> deadMongEvent(@PathVariable("mongId") Long mongId) {
        lifecycleService.dead(mongId);

        return ResponseEntity.ok().body(WakeupMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }
}
