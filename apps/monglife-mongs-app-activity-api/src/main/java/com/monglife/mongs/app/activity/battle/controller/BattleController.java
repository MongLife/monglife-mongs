package com.monglife.mongs.app.activity.battle.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.activity.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import com.monglife.mongs.app.activity.battle.service.BattleService;
import com.monglife.mongs.module.security.global.principal.Passport;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/activity/battle/match")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;


    /**
     * 매칭 대기열 등록
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 성공 응답
     */
    @PostMapping("/wait/{mongId}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> createWaitMatching(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1)  Long mongId
    ) {

        Long accountId = passport.getAccountId();
        String deviceId = passport.getDeviceId();

        battleService.createWaitMatching(accountId, mongId, deviceId);

        return ResponseEntity.ok().body(BattleResponse.ACTIVITY_BATTLE_CREATE_WAIT_MATCHING.toResponseDto());
    }

    /**
     * 매칭 대기열 삭제
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 성공 응답
     */
    @DeleteMapping("/wait/{mongId}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> deleteWaitMatching(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {

        Long accountId = passport.getAccountId();
        String deviceId = passport.getDeviceId();

        battleService.deleteWaitMatching(accountId, mongId, deviceId);

        return ResponseEntity.ok().body(BattleResponse.ACTIVITY_BATTLE_DELETE_WAIT_MATCHING.toResponseDto());
    }

    /**
     * 끝난 배틀 결과 조회
     * @param roomId 배틀 ID
     * @return 배틀 결과 응답
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDto<OverBattleResponseDto>> findOverBattle(@PathVariable("roomId") @NotNull @Min(1) Long roomId) {

        OverBattleDto overBattleDto = battleService.findOverBattle(roomId);

        OverBattleResponseDto overBattleResponseDto = OverBattleResponseDto.builder()
                .roomId(roomId)
                .winPlayerId(overBattleDto.getWinPlayerId())
                .winMongTypeCode(overBattleDto.getWinMongTypeCode())
                .build();

        return ResponseEntity.ok(BattleResponse.ACTIVITY_BATTLE_OVER_BATTLE.toResponseDto(overBattleResponseDto));
    }
}
