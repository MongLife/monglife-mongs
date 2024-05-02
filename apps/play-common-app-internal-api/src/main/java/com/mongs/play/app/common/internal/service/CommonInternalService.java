package com.mongs.play.app.common.internal.service;

import com.mongs.play.app.common.internal.data.FeedbackCodeData;
import com.mongs.play.app.common.internal.data.FoodCodeData;
import com.mongs.play.app.common.internal.data.MapCodeData;
import com.mongs.play.app.common.internal.data.MongCodeData;
import com.mongs.play.app.common.internal.vo.RegisterFeedbackCodeVo;
import com.mongs.play.app.common.internal.vo.RegisterFoodCodeVo;
import com.mongs.play.app.common.internal.vo.RegisterMapCodeVo;
import com.mongs.play.app.common.internal.vo.RegisterMongCodeVo;
import com.mongs.play.domain.code.entity.*;
import com.mongs.play.domain.code.service.CodeService;
import com.mongs.play.domain.code.service.CodeVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CommonInternalService {

    private final CodeVersionService codeVersionService;
    private final CodeService codeService;

    @Transactional
    public void resetCode() {
        String buildVersion = "1.0.0";

        codeVersionService.removeCodeVersion();
        codeService.removeMapCode();
        Arrays.stream(MapCodeData.values()).forEach(mapCode -> {
            codeService.addMapCode(MapCode.builder()
                    .buildVersion(buildVersion)
                    .code(mapCode.getCode())
                    .name(mapCode.getName())
                    .build());
        });

        codeService.removeMongCode();
        Arrays.stream(MongCodeData.values()).forEach(mongCode -> {
            codeService.addMongCode(MongCode.builder()
                    .buildVersion(buildVersion)
                    .code(mongCode.getCode())
                    .name(mongCode.getName())
                    .build());
        });

        codeService.removeFoodCode();
        Arrays.stream(FoodCodeData.values()).forEach(foodCode -> {
            codeService.addFoodCode(FoodCode.builder()
                    .buildVersion(buildVersion)
                    .code(foodCode.getCode())
                    .name(foodCode.getName())
                    .groupCode(foodCode.getGroupCode())
                    .addHealthyValue(foodCode.getAddHealthyValue())
                    .addSleepValue(foodCode.getAddSleepValue())
                    .addSatietyValue(foodCode.getAddSatietyValue())
                    .addWeightValue(foodCode.getAddWeightValue())
                    .addStrengthValue(foodCode.getAddStrengthValue())
                    .price(foodCode.getPrice())
                    .delaySeconds(foodCode.getDelaySeconds())
                    .build());
        });

        codeService.removeFeedbackCode();
        Arrays.stream(FeedbackCodeData.values()).forEach(feedbackCode -> {
            codeService.addFeedbackCode(FeedbackCode.builder()
                    .buildVersion(buildVersion)
                    .code(feedbackCode.getCode())
                    .groupCode(feedbackCode.getGroupCode())
                    .message(feedbackCode.getMessage())
                    .build());
        });

        codeVersionService.updateCode(buildVersion);
        codeVersionService.updateCodeWithUpdateApp(buildVersion);
    }

    @Transactional
    public RegisterMapCodeVo registerMapCode(String buildVersion, RegisterMapCodeVo registerMapCodeVo) {

        MapCode mapCode = codeService.addMapCode(MapCode.builder()
                .code(registerMapCodeVo.code())
                .name(registerMapCodeVo.name())
                .buildVersion(buildVersion)
                .build());

        codeVersionService.updateCodeWithUpdateApp(buildVersion);

        return RegisterMapCodeVo.builder()
                .code(mapCode.code())
                .name(mapCode.name())
                .build();
    }

    @Transactional
    public RegisterMongCodeVo registerMongCode(String buildVersion, RegisterMongCodeVo registerMongCodeVo) {

        MongCode mongCode = codeService.addMongCode(MongCode.builder()
                .code(registerMongCodeVo.code())
                .name(registerMongCodeVo.name())
                .buildVersion(buildVersion)
                .build());

        codeVersionService.updateCodeWithUpdateApp(buildVersion);

        return RegisterMongCodeVo.builder()
                .code(mongCode.code())
                .name(mongCode.name())
                .build();
    }

    @Transactional
    public RegisterFoodCodeVo registerFoodCode(String buildVersion, RegisterFoodCodeVo registerFoodCodeVo) {

        FoodCode foodCode = codeService.addFoodCode(FoodCode.builder()
                .code(registerFoodCodeVo.code())
                .name(registerFoodCodeVo.name())
                .groupCode(registerFoodCodeVo.groupCode())
                .price(registerFoodCodeVo.price())
                .addWeightValue(registerFoodCodeVo.addWeightValue())
                .addStrengthValue(registerFoodCodeVo.addStrengthValue())
                .addSatietyValue(registerFoodCodeVo.addSatietyValue())
                .addHealthyValue(registerFoodCodeVo.addHealthyValue())
                .addSleepValue(registerFoodCodeVo.addSleepValue())
                .delaySeconds(registerFoodCodeVo.delaySeconds())
                .buildVersion(buildVersion)
                .build());

        codeVersionService.updateCodeWithUpdateApp(buildVersion);

        return RegisterFoodCodeVo.builder()
                .code(foodCode.code())
                .name(foodCode.name())
                .groupCode(foodCode.groupCode())
                .price(foodCode.price())
                .addWeightValue(foodCode.addWeightValue())
                .addStrengthValue(foodCode.addStrengthValue())
                .addSatietyValue(foodCode.addSatietyValue())
                .addHealthyValue(foodCode.addHealthyValue())
                .addSleepValue(foodCode.addSleepValue())
                .delaySeconds(foodCode.delaySeconds())
                .build();
    }

    @Transactional
    public RegisterFeedbackCodeVo registerFeedbackCode(String buildVersion, RegisterFeedbackCodeVo registerFeedbackCodeVo) {

        FeedbackCode feedbackCode = codeService.addFeedbackCode(FeedbackCode.builder()
                .code(registerFeedbackCodeVo.code())
                .groupCode(registerFeedbackCodeVo.groupCode())
                .message(registerFeedbackCodeVo.message())
                .buildVersion(buildVersion)
                .build());

        codeVersionService.updateCode(buildVersion);

        return RegisterFeedbackCodeVo.builder()
                .code(feedbackCode.code())
                .groupCode(feedbackCode.groupCode())
                .message(feedbackCode.message())
                .build();
    }
}
