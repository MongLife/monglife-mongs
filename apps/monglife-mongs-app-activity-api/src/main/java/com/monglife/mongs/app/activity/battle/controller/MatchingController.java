package com.monglife.mongs.app.activity.battle.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import com.monglife.mongs.domain.battle.service.BattleService;
import com.monglife.mongs.domain.battle.service.MatchingService;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity/battle/match")
public class MatchingController {

    public final BattleService battleService;

    private final MatchingService matchingService;


    /**
     * 매칭 대기열 등록
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 성공 응답
     */
    @PostMapping("/wait/{mongId}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> createWaitMatching(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {

        Long accountId = passport.getAccountId();
        String deviceId = passport.getDeviceId();

        matchingService.createWaitMatching(accountId, deviceId, mongId);

        return ResponseEntity.ok().body(BattleResponse.ACTIVITY_BATTLE_CREATE_WAIT_MATCHING.toResponseDto());
    }

    /**
     * 매칭 대기열 삭제
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 성공 응답
     */
    @DeleteMapping("/wait/{mongId}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> deleteWaitMatching(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {

        Long accountId = passport.getAccountId();
        String deviceId = passport.getDeviceId();

        matchingService.deleteWaitMatching(accountId, deviceId, mongId);

        return ResponseEntity.ok().body(BattleResponse.ACTIVITY_BATTLE_DELETE_WAIT_MATCHING.toResponseDto());
    }
}
