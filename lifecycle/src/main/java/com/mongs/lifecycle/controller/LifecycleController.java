package com.mongs.lifecycle.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.lifecycle.controller.dto.response.*;
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

    /**
     * 몽 생성 시 호출
     *
     * @param mongId 몽 Id
     * @return {@link EggMongEventResDto}
     */
    @PostMapping("/egg/{mongId}")
    public ResponseEntity<EggMongEventResDto> eggMongEvent(
            @PathVariable("mongId") Long mongId
    ) {
        lifecycleService.eggEvent(mongId);

        return ResponseEntity.ok().body(EggMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    /**
     * 알에서 1단계로 진행하는 경우 호출
     *
     * @param mongId 몽 Id
     * @param passportDetail 사용자 인증 객체
     * @return {@link EvolutionMongEventResDto}
     */
    @PutMapping("/eggEvolution/{mongId}")
    public ResponseEntity<EvolutionMongEventResDto> eggEvolutionMongEvent(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        lifecycleService.eggEvolutionEvent(mongId, accountId);

        return ResponseEntity.ok().body(EvolutionMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    /**
     * 마지막 진화 단계에서 졸업 준비 단계로 진화하는 경우 호출
     *
     * @param mongId 몽 Id
     * @param passportDetail 사용자 인증 객체
     * @return {@link GraduationReadyMongEventResDto}
     */
    @DeleteMapping("/graduation/{mongId}")
    public ResponseEntity<GraduationReadyMongEventResDto> graduationReadyMongEvent(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        lifecycleService.graduationReadyEvent(mongId, accountId);

        return ResponseEntity.ok().body(GraduationReadyMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    /**
     * 수면 상태로 변경되는 경우 호출
     *
     * @param mongId 몽 Id
     * @param passportDetail 사용자 인증 객체
     * @return {@link SleepMongEventResDto}
     */
    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<SleepMongEventResDto> sleepMongEvent(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        lifecycleService.sleepEvent(mongId, accountId);

        return ResponseEntity.ok().body(SleepMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    /**
     * 비수면 상태로 변경되는 경우 호출
     *
     * @param mongId 몽 Id
     * @param passportDetail 사용자 인증 객체
     * @return {@link WakeupMongEventResDto}
     */
    @PutMapping("/wakeup/{mongId}")
    public ResponseEntity<WakeupMongEventResDto> wakeupMongEvent(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        lifecycleService.wakeupEvent(mongId, accountId);

        return ResponseEntity.ok().body(WakeupMongEventResDto.builder()
                .mongId(mongId)
                .build());
    }

    /**
     * 몽을 삭제하는 경우 호출
     *
     * @param mongId 몽 Id
     * @param passportDetail 사용자 인증 객체
     * @return {@link DeleteMongEventResDto}
     */
    @DeleteMapping("/delete/{mongId}")
    public ResponseEntity<DeleteMongEventResDto> deleteMongEvent(
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
