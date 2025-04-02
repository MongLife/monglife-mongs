package com.mongs.play.app.battle.worker.controller;

import com.mongs.play.app.battle.worker.code.BattleState;
import com.mongs.play.app.battle.worker.dto.res.MatchWaitResDto;
import com.mongs.play.app.battle.worker.service.BattleMatchService;
import com.mongs.play.app.battle.worker.service.BattleSearchService;
import com.mongs.play.app.battle.worker.vo.RegisterMatchWaitVo;
import com.mongs.play.app.battle.worker.vo.RemoveMatchWaitVo;
import com.mongs.play.client.publisher.battle.vo.res.MatchPlayerVo;
import com.mongs.play.client.publisher.battle.vo.res.MatchVo;
import com.mongs.play.domain.battle.vo.BattleRoomVo;
import com.mongs.play.module.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/battle")
@RequiredArgsConstructor
@RestController
public class BattleWorkerController {

    private final BattleSearchService battleSearchService;
    private final BattleMatchService battleMatchService;

    @PostMapping("/match/wait/{mongId}")
    public ResponseEntity<MatchWaitResDto> registerMatchWait(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @PathVariable("mongId") Long mongId
    ) {
        String deviceId = passportDetail.getDeviceId();

        RegisterMatchWaitVo registerMatchWaitVo = battleSearchService.registerMatchWait(deviceId, mongId);

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

        RemoveMatchWaitVo removeMatchWait = battleSearchService.removeMatchWait(deviceId, mongId);

        return ResponseEntity.ok().body(MatchWaitResDto.builder()
                .mongId(removeMatchWait.mongId())
                .deviceId(removeMatchWait.deviceId())
                .build());
    }

    @GetMapping("/match/{roomId}")
    public ResponseEntity<MatchVo> findMatchWait(@PathVariable("roomId") String roomId) {
        BattleRoomVo battleRoomVo = battleMatchService.findMatch(roomId);

        return ResponseEntity.ok().body(MatchVo.builder()
                .roomId(battleRoomVo.roomId())
                .round(battleRoomVo.round())
                .matchPlayerVoList(battleRoomVo.battlePlayerVoList().stream()
                        .map(battlePlayerVo -> MatchPlayerVo.builder()
                                .playerId(battlePlayerVo.playerId())
                                .mongCode(battlePlayerVo.mongCode())
                                .hp(battlePlayerVo.hp())
                                .state(BattleState.NONE.name())
                                .build())
                        .toList())
                .build());
    }
}
