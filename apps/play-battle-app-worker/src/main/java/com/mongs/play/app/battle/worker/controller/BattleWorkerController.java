package com.mongs.play.app.battle.worker.controller;

import com.mongs.play.app.battle.worker.dto.res.MatchWaitResDto;
import com.mongs.play.app.battle.worker.service.BattleSearchService;
import com.mongs.play.app.battle.worker.vo.RegisterMatchWaitVo;
import com.mongs.play.app.battle.worker.vo.RemoveMatchWaitVo;
import com.mongs.play.module.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/battle")
@RequiredArgsConstructor
@RestController
public class BattleWorkerController {

    private final BattleSearchService matchWaitService;

    @PostMapping("/match/wait/{mongId}")
    public ResponseEntity<MatchWaitResDto> registerMatchWait(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @PathVariable("mongId") Long mongId
    ) {
        String deviceId = passportDetail.getDeviceId();

        RegisterMatchWaitVo registerMatchWaitVo = matchWaitService.registerMatchWait(deviceId, mongId);

        return ResponseEntity.ok().body(MatchWaitResDto.builder()
                .mongId(registerMatchWaitVo.mongId())
                .deviceId(registerMatchWaitVo.deviceId())
                .build());
    }

    @DeleteMapping("/match/wait/{mongId}")
    public ResponseEntity<MatchWaitResDto> removeMatchWait(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @PathVariable("mongId") Long mongId
    ) {
        String deviceId = passportDetail.getDeviceId();

        RemoveMatchWaitVo removeMatchWait = matchWaitService.removeMatchWait(deviceId, mongId);

        return ResponseEntity.ok().body(MatchWaitResDto.builder()
                .mongId(removeMatchWait.mongId())
                .deviceId(removeMatchWait.deviceId())
                .build());
    }
}
