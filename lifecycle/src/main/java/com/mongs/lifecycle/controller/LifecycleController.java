package com.mongs.lifecycle.controller;

import com.mongs.lifecycle.dto.response.*;
import com.mongs.lifecycle.service.LifecycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lifecycle/admin")
public class LifecycleController {

    private final LifecycleService lifecycleService;

    @PostMapping("/egg/{mongId}")
    public ResponseEntity<Object> eggMongEvent(@PathVariable("mongId") Long mongId) {
        lifecycleService.eggEvent(mongId);

        return ResponseEntity.ok().body(EggMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @DeleteMapping("/graduation/{mongId}")
    public ResponseEntity<Object> graduationMongEvent(@PathVariable("mongId") Long mongId) {
        lifecycleService.graduationEvent(mongId);

        return ResponseEntity.ok().body(GraduationMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @DeleteMapping("/evolution/{mongId}")
    public ResponseEntity<Object> evolutionReadyMongEvent(@PathVariable("mongId") Long mongId) {
        lifecycleService.evolutionReadyEvent(mongId);

        return ResponseEntity.ok().body(EvolutionMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PutMapping("/evolution/{mongId}")
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

    @DeleteMapping("/dead/{mongId}")
    public ResponseEntity<Object> deadMongEvent(@PathVariable("mongId") Long mongId) {
        lifecycleService.dead(mongId);

        return ResponseEntity.ok().body(DeadMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }
}
