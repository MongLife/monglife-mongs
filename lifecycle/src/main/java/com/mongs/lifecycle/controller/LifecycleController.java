package com.mongs.lifecycle.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.lifecycle.dto.response.*;
import com.mongs.lifecycle.service.componentService.LifecycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lifecycle")
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
    public ResponseEntity<Object> graduationMongEvent(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        lifecycleService.graduationEvent(mongId, accountId);

        return ResponseEntity.ok().body(GraduationMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<Object> evolutionMongEvent(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        lifecycleService.evolutionEvent(mongId, accountId);

        return ResponseEntity.ok().body(EvolutionMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<Object> sleepMongEvent(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        lifecycleService.sleepEvent(mongId, accountId);

        return ResponseEntity.ok().body(SleepMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @PutMapping("/wakeup/{mongId}")
    public ResponseEntity<Object> wakeupMongEvent(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        lifecycleService.wakeupEvent(mongId, accountId);

        return ResponseEntity.ok().body(WakeupMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    @DeleteMapping("/delete/{mongId}")
    public ResponseEntity<Object> deleteMongEvent(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        lifecycleService.deleteEvent(mongId, accountId);

        return ResponseEntity.ok().body(DeleteMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }
}
