package com.mongs.play.app.battle.worker.controller;

import com.mongs.play.app.battle.worker.dto.req.MatchWaitReqDto;
import com.mongs.play.app.battle.worker.dto.res.MatchWaitResDto;
import com.mongs.play.app.battle.worker.service.BattleSearchService;
import com.mongs.play.app.battle.worker.vo.RegisterMatchWaitVo;
import com.mongs.play.app.battle.worker.vo.RemoveMatchWaitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/battle")
@RequiredArgsConstructor
@RestController
public class BattleWorkerController {

    private final BattleSearchService matchWaitService;

    @PostMapping("/match/wait")
    public ResponseEntity<MatchWaitResDto> registerMatchWait(@RequestBody MatchWaitReqDto matchWaitReqDto) {
        Long mongId = matchWaitReqDto.mongId();
        String deviceId = matchWaitReqDto.deviceId();

        RegisterMatchWaitVo registerMatchWaitVo = matchWaitService.registerMatchWait(deviceId, mongId);

        return ResponseEntity.ok().body(MatchWaitResDto.builder()
                .mongId(registerMatchWaitVo.mongId())
                .deviceId(registerMatchWaitVo.deviceId())
                .build());
    }

    @DeleteMapping("/match/wait")
    public ResponseEntity<MatchWaitResDto> removeMatchWait(@RequestBody MatchWaitReqDto matchWaitReqDto) {
        Long mongId = matchWaitReqDto.mongId();
        String deviceId = matchWaitReqDto.deviceId();

        RemoveMatchWaitVo removeMatchWait = matchWaitService.removeMatchWait(deviceId, mongId);

        return ResponseEntity.ok().body(MatchWaitResDto.builder()
                .mongId(removeMatchWait.mongId())
                .deviceId(removeMatchWait.deviceId())
                .build());
    }
}
