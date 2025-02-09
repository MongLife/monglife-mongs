package com.monglife.mongs.app.activity.training.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.training.dto.request.TrainingRunnerRequestDto;
import com.monglife.mongs.app.activity.training.dto.response.GetTrainingRunnerResponseDto;
import com.monglife.mongs.app.activity.training.enums.TrainingResponse;
import com.monglife.mongs.app.activity.training.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    /**
     * 훈련 달리기 정보 조회
     * @return 훈련 달리기 정보
     */
    @GetMapping("/runner")
    public ResponseEntity<ResponseDto<GetTrainingRunnerResponseDto>> getTrainingRunner() {

        Integer payPoint = trainingService.getTrainingRunnerPayPoint();

        GetTrainingRunnerResponseDto getTrainingRunnerResponseDto = GetTrainingRunnerResponseDto.builder()
                .payPoint(payPoint)
                .build();

        return ResponseEntity.ok(TrainingResponse.APP_ACTIVITY_GET_TRAINING_RUNNER.toResponseDto(getTrainingRunnerResponseDto));
    }

    /**
     * 훈련 달리기 완료
     * @param mongId 몽 ID
     * @param trainingRunnerRequestDto 훈련 달리기 완료 Dto
     * @return 성공 응답
     */
    @PostMapping("/runner/{mongId}")
    public ResponseEntity<ResponseDto<?>> trainingRunnerEnd(@PathVariable("mongId") Long mongId, @RequestBody TrainingRunnerRequestDto trainingRunnerRequestDto) {

        Integer score = trainingRunnerRequestDto.getScore();

        trainingService.trainingRunnerEnd(mongId, score);

        return ResponseEntity.ok(TrainingResponse.APP_ACTIVITY_TRAINING_RUNNER.toResponseDto());
    }
}
