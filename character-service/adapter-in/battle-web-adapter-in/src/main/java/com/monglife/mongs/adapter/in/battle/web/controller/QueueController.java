package com.monglife.mongs.adapter.in.battle.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.battle.web.enums.AdapterInBattleWebResponse;
import com.monglife.mongs.application.battle.port.in.QueueUseCase;
import com.monglife.mongs.application.battle.port.in.command.CreateQueuePlayerCommand;
import com.monglife.mongs.application.battle.port.in.command.DeleteQueuePlayerCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/battle/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueUseCase queueUseCase;

    /**
     * 매칭 대기열 등록
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 성공 응답
     */
    @PostMapping("/{mongId}")
    public ResponseEntity<ResponseDto<?>> createQueuePlayer(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1)  Long mongId) {

        CreateQueuePlayerCommand command = CreateQueuePlayerCommand.builder()
                .mongId(mongId)
                .deviceId(passport.getDeviceId())
                .accountId(passport.getAccountId())
                .build();

        queueUseCase.createQueuePlayerUseCase(command);

        return ResponseEntity.ok().body(AdapterInBattleWebResponse.CREATE_QUEUE_PLAYER.toResponseDto());
    }

    /**
     * 매칭 대기열 삭제
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 성공 응답
     */
    @DeleteMapping("/{mongId}")
    public ResponseEntity<ResponseDto<?>> deleteWaitMatching(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1) Long mongId) {

        DeleteQueuePlayerCommand command = DeleteQueuePlayerCommand.builder()
                .mongId(mongId)
                .deviceId(passport.getDeviceId())
                .accountId(passport.getAccountId())
                .build();

        queueUseCase.deleteQueuePlayerUseCase(command);

        return ResponseEntity.ok().body(AdapterInBattleWebResponse.DELETE_QUEUE_PLAYER.toResponseDto());
    }
}
