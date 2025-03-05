package com.monglife.mongs.app.activity.training.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.training.dto.request.TrainingBasketballRequestDto;
import com.monglife.mongs.app.activity.training.dto.request.TrainingRunnerRequestDto;
import com.monglife.mongs.app.activity.training.dto.response.GetTrainingResponseDto;
import com.monglife.mongs.app.activity.training.dto.response.TrainingBasketballEndResponseDto;
import com.monglife.mongs.app.activity.training.dto.response.TrainingRunnerEndResponseDto;
import com.monglife.mongs.app.activity.training.enums.TrainingCode;
import com.monglife.mongs.app.activity.training.enums.TrainingResponse;
import com.monglife.mongs.app.activity.training.service.TrainingService;
import com.monglife.mongs.app.activity.training.vo.TrainingEndVo;
import com.monglife.mongs.app.activity.training.vo.TrainingVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    /**
     * 훈련 정보 조회
     * @return 훈련 정보
     */
    @GetMapping("")
    public ResponseEntity<ResponseDto<GetTrainingResponseDto>> getTraining(@RequestParam("trainingCode") TrainingCode trainingCode) {

        TrainingVo trainingVo = trainingService.getTraining(trainingCode);

        GetTrainingResponseDto getTrainingResponseDto = GetTrainingResponseDto.builder()
                .rewardPayPoint(trainingVo.getRewardPayPoint())
                .score(trainingVo.getScore())
                .timeout(trainingVo.getTimeout())
                .build();

        return ResponseEntity.ok(TrainingResponse.APP_ACTIVITY_GET_TRAINING.toResponseDto(getTrainingResponseDto));
    }

    /**
     * 훈련 달리기 완료
     * @param trainingRunnerRequestDto 훈련 달리기 완료 Dto
     * @return 성공 응답
     */
    @PostMapping("/runner")
    public ResponseEntity<ResponseDto<TrainingRunnerEndResponseDto>> trainingRunnerEnd(@RequestBody TrainingRunnerRequestDto trainingRunnerRequestDto) {

        Long mongId = trainingRunnerRequestDto.getMongId();
        Integer score = trainingRunnerRequestDto.getScore();

        TrainingEndVo trainingEndVo = trainingService.trainingRunnerEnd(mongId, score);

        TrainingRunnerEndResponseDto trainingRunnerEndResponseDto = TrainingRunnerEndResponseDto.builder()
                .isSuccess(trainingEndVo.getIsSuccess())
                .rewardPayPoint(trainingEndVo.getRewardPayPoint())
                .score(trainingEndVo.getScore())
                .build();

        return ResponseEntity.ok(TrainingResponse.APP_ACTIVITY_TRAINING_RUNNER.toResponseDto(trainingRunnerEndResponseDto));
    }

    /**
     * 훈련 농구 완료
     * @param trainingBasketballRequestDto 훈련 농구 완료 Dto
     * @return 성공 응답
     */
    @PostMapping("/basketball")
    public ResponseEntity<ResponseDto<TrainingBasketballEndResponseDto>> trainingBasketballEnd(@RequestBody TrainingBasketballRequestDto trainingBasketballRequestDto) {

        Long mongId = trainingBasketballRequestDto.getMongId();
        Integer score = trainingBasketballRequestDto.getScore();

        TrainingEndVo trainingEndVo = trainingService.trainingBasketballEnd(mongId, score);

        TrainingBasketballEndResponseDto trainingBasketballEndResponseDto = TrainingBasketballEndResponseDto.builder()
                .isSuccess(trainingEndVo.getIsSuccess())
                .rewardPayPoint(trainingEndVo.getRewardPayPoint())
                .score(trainingEndVo.getScore())
                .build();

        return ResponseEntity.ok(TrainingResponse.APP_ACTIVITY_TRAINING_BASKETBALL.toResponseDto(trainingBasketballEndResponseDto));
    }
}
