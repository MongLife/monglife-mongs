package com.mongs.common.service;

import com.mongs.common.code.InitFeedbackCodeData;
import com.mongs.common.code.InitFoodCodeData;
import com.mongs.common.code.InitMapCodeData;
import com.mongs.common.code.InitMongCodeData;
import com.mongs.common.controller.dto.response.FindFeedbackCodeResDto;
import com.mongs.common.controller.dto.response.FindFoodCodeResDto;
import com.mongs.common.controller.dto.response.FindMapCodeResDto;
import com.mongs.common.controller.dto.response.FindMongCodeResDto;
import com.mongs.common.entity.CodeVersion;
import com.mongs.common.exception.CommonErrorCode;
import com.mongs.common.exception.NotFoundVersionException;
import com.mongs.common.repository.*;
import com.mongs.common.vo.FindVersionVo;
import com.mongs.core.entity.FeedbackCode;
import com.mongs.core.entity.FoodCode;
import com.mongs.core.entity.MapCode;
import com.mongs.core.entity.MongCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

    private final CodeVersionRepository codeVersionRepository;
    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;
    private final FoodCodeRepository foodCodeRepository;
    private final FeedbackCodeRepository feedbackCodeRepository;

    /**
     * 1 번째 코드 값 : 앱 업데이트 여부 판별
     * 2 번째 코드 값 : 코드 업데이트 여부 판별
     * 3 번째 코드 값 : 에러 수정 (영향 없음)
     * **/
    public FindVersionVo findVersion(String buildVersion) {
        CodeVersion codeVersion = codeVersionRepository.findTopByOrderByBuildVersionDesc()
                .orElseThrow(() -> new NotFoundVersionException(CommonErrorCode.NOT_FOUND_VERSION));

        int clientVersionFirst = Integer.parseInt(buildVersion.split("\\.")[0]);
        int serverVersionFirst = Integer.parseInt(codeVersion.buildVersion().split("\\.")[0]);
        boolean mustUpdateApp = clientVersionFirst < serverVersionFirst;

        int clientVersionSecond = Integer.parseInt(buildVersion.split("\\.")[1]);
        int serverVersionSecond = Integer.parseInt(codeVersion.buildVersion().split("\\.")[1]);
        boolean mustUpdateCode = clientVersionSecond < serverVersionSecond;

        return FindVersionVo.builder()
                .newestBuildVersion(codeVersion.buildVersion())
                .createdAt(codeVersion.createdAt())
                .mustUpdateApp(mustUpdateApp)
                .mustUpdateCode(mustUpdateCode || mustUpdateApp)
                .build();
    }

    public List<FindMapCodeResDto> findMapCode() {
        return FindMapCodeResDto.toList(mapCodeRepository.findAll());
    }

    public List<FindMongCodeResDto> findMongCode() {
        return FindMongCodeResDto.toList(mongCodeRepository.findAll());
    }

    public List<FindFoodCodeResDto> findFoodCode() {
        return FindFoodCodeResDto.toList(foodCodeRepository.findAll());
    }

    public List<FindFeedbackCodeResDto> findFeedbackCode() {
        return FindFeedbackCodeResDto.toList(feedbackCodeRepository.findAll());
    }

    public void initializeCode() {
        String initBuildVersion = "1.1.0";

        codeVersionRepository.deleteAll();
        codeVersionRepository.save(CodeVersion.builder()
                .buildVersion(initBuildVersion)
                .createdAt(LocalDateTime.now())
                .build());

        foodCodeRepository.deleteAll();
        Arrays.stream(InitFoodCodeData.values()).forEach(foodCode -> {
            foodCodeRepository.save(FoodCode.builder()
                    .buildVersion(initBuildVersion)
                    .code(foodCode.getCode())
                    .name(foodCode.getName())
                    .groupCode(foodCode.getGroupCode())
                    .addHealthyValue(foodCode.getAddHealthyValue())
                    .addSleepValue(foodCode.getAddSleepValue())
                    .addSatietyValue(foodCode.getAddSatietyValue())
                    .addWeightValue(foodCode.getAddWeightValue())
                    .addStrengthValue(foodCode.getAddStrengthValue())
                    .price(foodCode.getPrice())
                    .build());
        });

        mapCodeRepository.deleteAll();
        Arrays.stream(InitMapCodeData.values()).forEach(mapCode -> {
            mapCodeRepository.save(MapCode.builder()
                    .buildVersion(initBuildVersion)
                    .code(mapCode.getCode())
                    .name(mapCode.getName())
                    .build());
        });

        mongCodeRepository.deleteAll();
        Arrays.stream(InitMongCodeData.values()).forEach(mongCode -> {
            mongCodeRepository.save(MongCode.builder()
                    .buildVersion(initBuildVersion)
                    .code(mongCode.getCode())
                    .name(mongCode.getName())
                    .build());
        });

        feedbackCodeRepository.deleteAll();
        Arrays.stream(InitFeedbackCodeData.values()).forEach(feedbackCode -> {
            feedbackCodeRepository.save(FeedbackCode.builder()
                    .buildVersion(initBuildVersion)
                    .code(feedbackCode.getCode())
                    .groupCode(feedbackCode.getGroupCode())
                    .message(feedbackCode.getMessage())
                    .build());
        });
    }
}
