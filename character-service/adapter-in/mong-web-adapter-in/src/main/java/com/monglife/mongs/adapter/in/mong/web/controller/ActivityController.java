package com.monglife.mongs.adapter.in.mong.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.mong.web.dto.request.TrainingEndRequestDto;
import com.monglife.mongs.adapter.in.mong.web.dto.response.GetTrainingTypeResponseDto;
import com.monglife.mongs.adapter.in.mong.web.dto.response.TrainingEndResponseDto;
import com.monglife.mongs.adapter.in.mong.web.enums.AdapterInMongWebResponse;
import com.monglife.mongs.application.mong.port.in.ActivityUseCase;
import com.monglife.mongs.application.mong.port.in.command.GetTrainingTypeCommand;
import com.monglife.mongs.application.mong.port.in.command.TrainingEndCommand;
import com.monglife.mongs.domain.mong.model.Mong;
import com.monglife.mongs.domain.mong.model.TrainingType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityUseCase activityUseCase;

    /**
     * 훈련 목록 조회
     * @return 훈련 정보 목록
     */
    @GetMapping
    public ResponseEntity<ResponseDto<List<GetTrainingTypeResponseDto>>> getTraining() {

        List<GetTrainingTypeResponseDto> getTrainingTypeResponseDtos = activityUseCase.getTrainingTypesUseCase().stream()
                .map(trainingType -> GetTrainingTypeResponseDto.builder()
                        .trainingTypeId(trainingType.getTrainingTypeId())
                        .trainingTypeCode(trainingType.getTrainingTypeCode())
                        .trainingTypeName(trainingType.getTrainingTypeName())
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
    @GetMapping("/{trainingTypeCode}")
    public ResponseEntity<ResponseDto<GetTrainingTypeResponseDto>> getTraining(@PathVariable("trainingTypeCode") String trainingTypeCode) {

        GetTrainingTypeCommand command = GetTrainingTypeCommand.builder()
                .trainingTypeCode(trainingTypeCode)
                .build();

        TrainingType trainingType = activityUseCase.getTrainingTypeUseCase(command);

        GetTrainingTypeResponseDto getTrainingTypeResponseDto = GetTrainingTypeResponseDto.builder()
                .trainingTypeId(trainingType.getTrainingTypeId())
                .trainingTypeCode(trainingType.getTrainingTypeCode())
                .trainingTypeName(trainingType.getTrainingTypeName())
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
    @PostMapping
    public ResponseEntity<ResponseDto<TrainingEndResponseDto>> trainingRunnerEnd(@AuthenticationPrincipal Passport passport, @RequestBody TrainingEndRequestDto trainingEndRequestDto) {

        TrainingEndCommand command = TrainingEndCommand.builder()
                .accountId(passport.getAccountId())
                .trainingTypeCode(trainingEndRequestDto.getTrainingTypeCode())
                .mongId(trainingEndRequestDto.getMongId())
                .score(trainingEndRequestDto.getScore())
                .build();

        Mong mong = activityUseCase.trainingEndUseCase(command);

        TrainingEndResponseDto trainingEndResponseDto = TrainingEndResponseDto.builder()
                .mongId(mong.getMongId())
                .payPoint(mong.getPayPoint())
                .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                .strengthRatio(mong.getStrength() / mong.getMaxStatus() * 100)
                .healthyRatio(mong.getHealthy() / mong.getMaxStatus() * 100)
                .satietyRatio(mong.getSatiety() / mong.getMaxStatus() * 100)
                .fatigueRatio(mong.getFatigue() / mong.getMaxStatus() * 100)
                .weight(mong.getWeight())
                .stateCode(mong.getStateCode())
                .statusCode(mong.getStatusCode())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.TRAINING_END.toResponseDto(trainingEndResponseDto));
    }
}
