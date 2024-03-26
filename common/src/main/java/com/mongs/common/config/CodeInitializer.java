package com.mongs.common.config;

import com.mongs.common.entity.CodeVersion;
import com.mongs.common.repository.*;
import com.mongs.core.entity.FeedbackCode;
import com.mongs.core.entity.FoodCode;
import com.mongs.core.entity.MapCode;
import com.mongs.core.entity.MongCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CodeInitializer implements ApplicationRunner {

    private final CodeVersionRepository codeVersionRepository;
    private final FoodCodeRepository foodCodeRepository;
    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;
    private final FeedbackCodeRepository feedbackCodeRepository;

    @Override
    public void run(ApplicationArguments args) {

        String buildVersion = "1.0.0";
        codeVersionRepository.deleteAll();
        codeVersionRepository.save(CodeVersion.builder()
                .buildVersion(buildVersion)
                .createdAt(LocalDateTime.now())
                .build());

        foodCodeRepository.deleteAll();
        Arrays.stream(InitFoodCodeData.values()).forEach(foodCode -> {
            foodCodeRepository.save(FoodCode.builder()
                    .buildVersion(buildVersion)
                    .code(foodCode.getCode())
                    .name(foodCode.getName())
                    .groupCode(foodCode.getGroupCode())
                    .addHealthyValue(foodCode.getAddHealthyValue())
                    .addSleepValue(foodCode.getAddSleepValue())
                    .addSatietyValue(foodCode.getAddSatietyValue())
                    .addWeightValue(foodCode.getAddWeightValue())
                    .addStrengthValue(foodCode.getAddStrengthValue())
                    .price(foodCode.getPoint())
                    .build());
        });

        mapCodeRepository.deleteAll();
        Arrays.stream(InitMapCodeData.values()).forEach(mapCode -> {
            mapCodeRepository.save(MapCode.builder()
                    .buildVersion(buildVersion)
                    .code(mapCode.getCode())
                    .name(mapCode.getName())
                    .build());
        });

        mongCodeRepository.deleteAll();
        Arrays.stream(InitMongCodeData.values()).forEach(mongCode -> {
            mongCodeRepository.save(MongCode.builder()
                    .buildVersion(buildVersion)
                    .code(mongCode.getCode())
                    .name(mongCode.getName())
                    .build());
        });

        feedbackCodeRepository.deleteAll();
        Arrays.stream(InitFeedbackCodeData.values()).forEach(feedbackCode -> {
            feedbackCodeRepository.save(FeedbackCode.builder()
                    .buildVersion(buildVersion)
                    .code(feedbackCode.getCode())
                    .groupCode(feedbackCode.getGroupCode())
                    .message(feedbackCode.getMessage())
                    .build());
        });
    }
}
