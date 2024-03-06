package com.mongs.lifecycle.controller;

import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.dto.response.*;
import com.mongs.lifecycle.service.MongEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lifecycle")
public class LifeCycleController {

    private final MongEventService mongEventService;

    @GetMapping("/stress_test/{size}")
    public ResponseEntity<Object> stressTest(@PathVariable("size") Integer size) {

        for (long mongId = 1; mongId <= Math.min(1000, size); mongId++) {
            mongEventService.removeAllMongEvent(mongId);
            mongEventService.registerMongEvent(mongId, MongEventCode.WEIGHT_DOWN);
            mongEventService.registerMongEvent(mongId, MongEventCode.HEALTHY_DOWN);
            mongEventService.registerMongEvent(mongId, MongEventCode.STRENGTH_DOWN);
            mongEventService.registerMongEvent(mongId, MongEventCode.SATIETY_DOWN);
            mongEventService.registerMongEvent(mongId, MongEventCode.SLEEP_DOWN);
            mongEventService.registerMongEvent(mongId, MongEventCode.POOP);
        }

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/status/{mongId}")
    public ResponseEntity<Object> statusMongEvent(@PathVariable("mongId") Long mongId) {
        mongEventService.registerMongEvent(mongId, MongEventCode.WEIGHT_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.HEALTHY_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.STRENGTH_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.SATIETY_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.SLEEP_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.POOP);

        return ResponseEntity.ok().body(RegisterMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<Object> sleepMongEvent(@PathVariable("mongId") Long mongId) {
        mongEventService.removeMongEvent(mongId, MongEventCode.WEIGHT_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.HEALTHY_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.STRENGTH_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.SATIETY_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.SLEEP_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.POOP);
        mongEventService.registerMongEvent(mongId, MongEventCode.SLEEP_UP);

        return ResponseEntity.ok().body(SleepMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PutMapping("/wakeup/{mongId}")
    public ResponseEntity<Object> wakeupMongEvent(@PathVariable("mongId") Long mongId) {
        mongEventService.removeMongEvent(mongId, MongEventCode.SLEEP_UP);
        mongEventService.registerMongEvent(mongId, MongEventCode.WEIGHT_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.HEALTHY_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.STRENGTH_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.SATIETY_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.SLEEP_DOWN);
        mongEventService.registerMongEvent(mongId, MongEventCode.POOP);

        return ResponseEntity.ok().body(WakeupMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @DeleteMapping("/evolution/{mongId}")
    public ResponseEntity<Object> evolutionMongEvent(@PathVariable("mongId") Long mongId) {
        mongEventService.removeMongEvent(mongId, MongEventCode.WEIGHT_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.STRENGTH_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.SATIETY_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.HEALTHY_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.SLEEP_DOWN);
        mongEventService.removeMongEvent(mongId, MongEventCode.POOP);

        return ResponseEntity.ok().body(EvolutionMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @DeleteMapping("/graduation/{mongId}")
    public ResponseEntity<Object> graduationMongEvent(@PathVariable("mongId") Long mongId) {
        mongEventService.removeAllMongEvent(mongId);

        return ResponseEntity.ok().body(GraduationMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @DeleteMapping("/dead/{mongId}")
    public ResponseEntity<Object> deadMongEvent(@PathVariable("mongId") Long mongId) {
        mongEventService.registerMongEvent(mongId, MongEventCode.ADMIN_DEAD);
        mongEventService.removeAllMongEvent(mongId);

        return ResponseEntity.ok().body(DeadMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }
}
