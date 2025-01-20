package com.monglife.mongs.app.activity.training.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.training.dto.request.TrainingRunnerRequestDto;
import com.monglife.mongs.app.activity.training.enums.TrainingResponse;
import com.monglife.mongs.app.activity.training.service.TrainingService;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    /**
     * 훈련 달리기 완료
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @param trainingRunnerRequestDto 훈련 달리기 완료 Dto
     * @return 성공 응답
     */
    @PostMapping("/runner/{mongId}")
    public ResponseEntity<ResponseDto<?>> trainingRunner(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId, @RequestBody TrainingRunnerRequestDto trainingRunnerRequestDto) {

        Long accountId = passport.getAccountId();
        Integer score = trainingRunnerRequestDto.getScore();

        trainingService.trainingRunner(accountId, mongId, score);

        return ResponseEntity.ok(TrainingResponse.APP_ACTIVITY_TRAINING_END_RUNNER.toResponseDto());
    }
}
