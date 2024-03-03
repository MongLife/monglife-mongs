package com.mongs.common.config;

import com.mongs.common.entity.CodeVersion;
import com.mongs.common.repository.*;
import com.mongs.core.code.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CodeInitializer implements ApplicationRunner {

    private final CodeVersionRepository codeVersionRepository;
    private final FoodCodeRepository foodCodeRepository;
    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;

    @Override
    public void run(ApplicationArguments args) {

        codeVersionRepository.deleteAll();
        codeVersionRepository.save(CodeVersion.builder()
                .version(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build());

        foodCodeRepository.deleteAll();
        Arrays.stream(InitFoodCodeData.values()).forEach(foodCode -> {
            foodCodeRepository.save(FoodCode.builder()
                    .code(foodCode.getCode())
                    .name(foodCode.getName())
                    .groupCode(foodCode.getGroupCode())
                    .fullness(foodCode.getFullness())
                    .point(foodCode.getPoint())
                    .build());
        });

        mapCodeRepository.deleteAll();
        Arrays.stream(InitMapCodeData.values()).forEach(mapCode -> {
            mapCodeRepository.save(MapCode.builder()
                            .code(mapCode.getCode())
                            .name(mapCode.getName())
                    .build());
        });

        mongCodeRepository.deleteAll();
        Arrays.stream(InitMongCodeData.values()).forEach(mongCode -> {
            mongCodeRepository.save(MongCode.builder()
                            .code(mongCode.getCode())
                            .name(mongCode.getName())
                    .build());
        });
    }
}
