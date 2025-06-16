package com.monglife.mongs.adapter.in.mong.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.mong.web.dto.request.TrainingEndRequestDto;
import com.monglife.mongs.adapter.in.mong.web.dto.response.GetTrainingTypeResponseDto;
import com.monglife.mongs.adapter.in.mong.web.dto.response.TrainingEndResponseDto;
import com.monglife.mongs.adapter.in.mong.web.enums.AdapterInMongWebResponse;
import com.monglife.mongs.application.mong.port.in.ActivityUseCase;
import com.monglife.mongs.application.mong.port.in.command.GetTrainingTypeCommand;
import com.monglife.mongs.application.mong.port.in.command.TrainingEndCommand;
import com.monglife.mongs.application.mong.port.in.vo.TrainingEndVo;
import com.monglife.mongs.domain.mong.model.TrainingType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityUseCase activityUseCase;

    /**
     * 훈련 목록 조회
     * @return 훈련 정보 목록
     */
    @EntryLoggingPoint
    @GetMapping("/training")
    public ResponseEntity<ResponseDto<List<GetTrainingTypeResponseDto>>> getTraining() {

        List<GetTrainingTypeResponseDto> getTrainingTypeResponseDtos = activityUseCase.getTrainingTypesUseCase().stream()
                .map(trainingType -> GetTrainingTypeResponseDto.builder()
                        .trainingTypeId(trainingType.getTrainingTypeId())
                        .trainingCode(trainingType.getTrainingCode())
                        .trainingName(trainingType.getTrainingName())
                        .payPoint(trainingType.getPayPoint())
                        .score(trainingType.getScore())
                        .timeout(trainingType.getTimeout())
                        .exp(trainingType.getExp())
                        .strength(trainingType.getStrength())
                        .weight(trainingType.getWeight())
                        .satiety(trainingType.getSatiety())
                        .fatigue(trainingType.getFatigue())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInMongWebResponse.GET_TRAINING_TYPES.toResponseDto(getTrainingTypeResponseDtos));
    }

    /**
     * 훈련 조회
     * @return 훈련 정보
     */
    @EntryLoggingPoint
    @GetMapping("/training/{trainingCode}")
    public ResponseEntity<ResponseDto<GetTrainingTypeResponseDto>> getTraining(@PathVariable("trainingCode") @NotBlank String trainingCode) {

        GetTrainingTypeCommand command = GetTrainingTypeCommand.builder()
                .trainingCode(trainingCode)
                .build();

        TrainingType trainingType = activityUseCase.getTrainingTypeUseCase(command);

        GetTrainingTypeResponseDto getTrainingTypeResponseDto = GetTrainingTypeResponseDto.builder()
                .trainingTypeId(trainingType.getTrainingTypeId())
                .trainingCode(trainingType.getTrainingCode())
                .trainingName(trainingType.getTrainingName())
                .payPoint(trainingType.getPayPoint())
                .score(trainingType.getScore())
                .timeout(trainingType.getTimeout())
                .exp(trainingType.getExp())
                .strength(trainingType.getStrength())
                .weight(trainingType.getWeight())
                .satiety(trainingType.getSatiety())
                .fatigue(trainingType.getFatigue())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.GET_TRAINING_TYPE.toResponseDto(getTrainingTypeResponseDto));
    }

    /**
     * 훈련 완료
     */
    @EntryLoggingPoint
    @PostMapping("/training")
    public ResponseEntity<ResponseDto<TrainingEndResponseDto>> trainingRunnerEnd(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody TrainingEndRequestDto trainingEndRequestDto
    ) {

        TrainingEndCommand command = TrainingEndCommand.builder()
                .accountId(passport.getAccountId())
                .trainingCode(trainingEndRequestDto.getTrainingCode())
                .mongId(trainingEndRequestDto.getMongId())
                .score(trainingEndRequestDto.getScore())
                .build();

        TrainingEndVo trainingEndVo = activityUseCase.trainingEndUseCase(command);

        TrainingEndResponseDto trainingEndResponseDto = TrainingEndResponseDto.builder()
                .isSuccess(trainingEndVo.getIsSuccess())
                .rewardPayPoint(trainingEndVo.getRewardPayPoint())
                .score(trainingEndVo.getScore())
                .mongId(trainingEndVo.getMong().getMongId())
                .payPoint(trainingEndVo.getMong().getPayPoint())
                .expRatio(trainingEndVo.getMong().getExp() / trainingEndVo.getMong().getMaxStatus() * 100)
                .strengthRatio(trainingEndVo.getMong().getStrength() / trainingEndVo.getMong().getMaxStatus() * 100)
                .healthyRatio(trainingEndVo.getMong().getHealthy() / trainingEndVo.getMong().getMaxStatus() * 100)
                .satietyRatio(trainingEndVo.getMong().getSatiety() / trainingEndVo.getMong().getMaxStatus() * 100)
                .fatigueRatio(trainingEndVo.getMong().getFatigue() / trainingEndVo.getMong().getMaxStatus() * 100)
                .weight(trainingEndVo.getMong().getWeight())
                .stateCode(trainingEndVo.getMong().getStateCode())
                .statusCode(trainingEndVo.getMong().getStatusCode())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.TRAINING_END.toResponseDto(trainingEndResponseDto));
    }
}
