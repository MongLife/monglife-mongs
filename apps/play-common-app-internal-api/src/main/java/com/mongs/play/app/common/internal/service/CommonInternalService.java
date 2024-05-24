package com.mongs.play.app.common.internal.service;

import com.mongs.play.app.common.internal.data.FoodCodeData;
import com.mongs.play.app.common.internal.data.MapCodeData;
import com.mongs.play.app.common.internal.data.MongCodeData;
import com.mongs.play.app.common.internal.vo.*;
import com.mongs.play.domain.code.entity.CodeVersion;
import com.mongs.play.domain.code.entity.FoodCode;
import com.mongs.play.domain.code.entity.MapCode;
import com.mongs.play.domain.code.entity.MongCode;
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
    public RegisterCodeVersionVo registerCodeVersion(String buildVersion) {

        CodeVersion codeVersion = codeVersionService.addCodeVersion(buildVersion);

        return RegisterCodeVersionVo.builder()
                .buildVersion(codeVersion.getBuildVersion())
                .build();
    }

    @Transactional
    public RegisterMapCodeVo registerMapCode(String buildVersion, RegisterMapCodeVo registerMapCodeVo) {

        CodeVersion codeVersion = codeVersionService.getCodeVersion(buildVersion);

        MapCode mapCode = codeService.addMapCode(MapCode.builder()
                .code(registerMapCodeVo.code())
                .name(registerMapCodeVo.name())
                .buildVersion(codeVersion.getBuildVersion())
                .build());

        codeVersionService.updateCode(buildVersion);

        return RegisterMapCodeVo.builder()
                .code(mapCode.getCode())
                .name(mapCode.getName())
                .buildVersion(codeVersion.getBuildVersion())
                .build();
    }

    @Transactional
    public RegisterMongCodeVo registerMongCode(String buildVersion, RegisterMongCodeVo registerMongCodeVo) {

        CodeVersion codeVersion = codeVersionService.getCodeVersion(buildVersion);

        MongCode mongCode = codeService.addMongCode(MongCode.builder()
                .code(registerMongCodeVo.code())
                .name(registerMongCodeVo.name())
                .level(registerMongCodeVo.level())
                .evolutionPoint(registerMongCodeVo.evolutionPoint())
                .buildVersion(codeVersion.getBuildVersion())
                .build());

        codeVersionService.updateCode(buildVersion);

        return RegisterMongCodeVo.builder()
                .code(mongCode.getCode())
                .name(mongCode.getName())
                .level(mongCode.getLevel())
                .evolutionPoint(mongCode.getEvolutionPoint())
                .buildVersion(codeVersion.getBuildVersion())
                .build();
    }

    @Transactional
    public RegisterFoodCodeVo registerFoodCode(String buildVersion, RegisterFoodCodeVo registerFoodCodeVo) {

        CodeVersion codeVersion = codeVersionService.getCodeVersion(buildVersion);

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
                .buildVersion(codeVersion.getBuildVersion())
                .build());

        codeVersionService.updateCode(buildVersion);

        return RegisterFoodCodeVo.builder()
                .code(foodCode.getCode())
                .name(foodCode.getName())
                .groupCode(foodCode.getGroupCode())
                .price(foodCode.getPrice())
                .addWeightValue(foodCode.getAddWeightValue())
                .addStrengthValue(foodCode.getAddStrengthValue())
                .addSatietyValue(foodCode.getAddSatietyValue())
                .addHealthyValue(foodCode.getAddHealthyValue())
                .addSleepValue(foodCode.getAddSleepValue())
                .delaySeconds(foodCode.getDelaySeconds())
                .buildVersion(codeVersion.getBuildVersion())
                .build();
    }

    @Transactional
    public void resetCode() {
        String buildVersion = "1.0.0";

        codeService.removeMapCode();
        Arrays.stream(MapCodeData.values()).forEach(mapCode -> codeService.addMapCode(MapCode.builder()
                    .buildVersion(buildVersion)
                    .code(mapCode.getCode())
                    .name(mapCode.getName())
                    .build()));

        codeService.removeMongCode();
        Arrays.stream(MongCodeData.values()).forEach(mongCode -> codeService.addMongCode(MongCode.builder()
                    .buildVersion(buildVersion)
                    .code(mongCode.getCode())
                    .name(mongCode.getName())
                    .level(mongCode.getLevel())
                    .evolutionPoint(mongCode.getEvolutionPoint())
                    .build()));

        codeService.removeFoodCode();
        Arrays.stream(FoodCodeData.values()).forEach(foodCode -> codeService.addFoodCode(FoodCode.builder()
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
                    .build()));

        codeVersionService.removeCodeVersion();
        codeVersionService.addCodeVersion(buildVersion);

        codeVersionService.updateCode(buildVersion);
    }
}
