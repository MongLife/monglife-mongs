package com.monglife.mongs.adapter.in.mong.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.mong.web.dto.request.CreateMongRequestDto;
import com.monglife.mongs.adapter.in.mong.web.dto.response.*;
import com.monglife.mongs.adapter.in.mong.web.enums.AdapterInMongWebResponse;
import com.monglife.mongs.application.mong.port.in.ManagementUseCase;
import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.domain.mong.model.Mong;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementUseCase managementUseCase;

    /**
     * 몽 목록 조회
     */
    @EntryLoggingPoint
    @GetMapping
    public ResponseEntity<ResponseDto<List<GetMongResponseDto>>> getMongs(@AuthenticationPrincipal Passport passport) {

        GetMongsCommand command = GetMongsCommand.builder()
                .accountId(passport.getAccountId())
                .build();

        List<GetMongResponseDto> getMongResponseDtos = managementUseCase.getMongsUseCase(command).stream()
                .map(mong -> GetMongResponseDto.builder()
                        .mongId(mong.getMongId())
                        .name(mong.getName())
                        .mongCode(mong.getMongCode())
                        .mongName(mong.getMongName())
                        .sleepAt(mong.getSleepAt())
                        .wakeupAt(mong.getWakeupAt())
                        .payPoint(mong.getPayPoint())
                        .level(mong.getLevel())
                        .randomDrawTicketCount(mong.getRandomDrawTicketCount())
                        .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                        .strengthRatio(mong.getStrength() / mong.getMaxStatus() * 100)
                        .healthyRatio(mong.getHealthy() / mong.getMaxStatus() * 100)
                        .satietyRatio(mong.getSatiety() / mong.getMaxStatus() * 100)
                        .fatigueRatio(mong.getFatigue() / mong.getMaxStatus() * 100)
                        .weight(mong.getWeight())
                        .stateCode(mong.getStateCode())
                        .statusCode(mong.getStatusCode())
                        .poopCount(mong.getPoopCount())
                        .isSleep(mong.getIsSleep())
                        .createdAt(mong.getCreatedAt())
                        .updatedAt(mong.getUpdatedAt())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInMongWebResponse.GET_MONGS.toResponseDto(getMongResponseDtos));
    }

    /**
     * 몽 단건 조회
     */
    @EntryLoggingPoint
    @GetMapping("/{mongId}")
    public ResponseEntity<ResponseDto<GetMongResponseDto>> getMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {

        GetMongCommand command = GetMongCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        Mong mong = managementUseCase.getMongUseCase(command);

        GetMongResponseDto getMongResponseDto = GetMongResponseDto.builder()
                .mongId(mong.getMongId())
                .name(mong.getName())
                .mongCode(mong.getMongCode())
                .mongName(mong.getMongName())
                .sleepAt(mong.getSleepAt())
                .wakeupAt(mong.getWakeupAt())
                .payPoint(mong.getPayPoint())
                .level(mong.getLevel())
                .randomDrawTicketCount(mong.getRandomDrawTicketCount())
                .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                .strengthRatio(mong.getStrength() / mong.getMaxStatus() * 100)
                .healthyRatio(mong.getHealthy() / mong.getMaxStatus() * 100)
                .satietyRatio(mong.getSatiety() / mong.getMaxStatus() * 100)
                .fatigueRatio(mong.getFatigue() / mong.getMaxStatus() * 100)
                .weight(mong.getWeight())
                .stateCode(mong.getStateCode())
                .statusCode(mong.getStatusCode())
                .poopCount(mong.getPoopCount())
                .isSleep(mong.getIsSleep())
                .createdAt(mong.getCreatedAt())
                .updatedAt(mong.getUpdatedAt())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.GET_MONG.toResponseDto(getMongResponseDto));
    }

    /**
     * 몽 생성
     */
    @EntryLoggingPoint
    @PostMapping
    public ResponseEntity<ResponseDto<CreateMongResponseDto>> createMong(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody CreateMongRequestDto createMongRequestDto
    ) {
        CreateMongCommand command = CreateMongCommand.builder()
                .accountId(passport.getAccountId())
                .name(createMongRequestDto.getName())
                .sleepAt(createMongRequestDto.getSleepAt())
                .wakeupAt(createMongRequestDto.getWakeupAt())
                .build();

        Mong mong = managementUseCase.createMongUseCase(command);

        CreateMongResponseDto createMongResponseDto = CreateMongResponseDto.builder()
                .mongId(mong.getMongId())
                .name(mong.getName())
                .mongCode(mong.getMongCode())
                .mongName(mong.getMongName())
                .payPoint(mong.getPayPoint())
                .level(mong.getLevel())
                .randomDrawTicketCount(mong.getRandomDrawTicketCount())
                .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                .strengthRatio(mong.getStrength() / mong.getMaxStatus() * 100)
                .healthyRatio(mong.getHealthy() / mong.getMaxStatus() * 100)
                .satietyRatio(mong.getSatiety() / mong.getMaxStatus() * 100)
                .fatigueRatio(mong.getFatigue() / mong.getMaxStatus() * 100)
                .weight(mong.getWeight())
                .sleepAt(mong.getSleepAt())
                .wakeupAt(mong.getWakeupAt())
                .stateCode(mong.getStateCode())
                .statusCode(mong.getStatusCode())
                .poopCount(mong.getPoopCount())
                .isSleep(mong.getIsSleep())
                .createdAt(mong.getCreatedAt())
                .updatedAt(mong.getUpdatedAt())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.CREATE_MONG.toResponseDto(createMongResponseDto));
    }

    /**
     * 몽 삭제
     */
    @EntryLoggingPoint
    @DeleteMapping("/{mongId}")
    public ResponseEntity<ResponseDto<DeleteMongResponseDto>> deleteMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {
        DeleteMongCommand command = DeleteMongCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        Mong mong = managementUseCase.deleteMongUseCase(command);

        DeleteMongResponseDto deleteMongResponseDto = DeleteMongResponseDto.builder()
                .mongId(mong.getMongId())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.DELETE_MONG.toResponseDto(deleteMongResponseDto));
    }

    /**
     * 몽 쓰다 듬기
     */
    @EntryLoggingPoint
    @PostMapping("/stroke/{mongId}")
    public ResponseEntity<ResponseDto<StrokeMongResponseDto>> strokeMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {
        StrokeMongCommand command = StrokeMongCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        Mong mong = managementUseCase.strokeMongUseCase(command);

        StrokeMongResponseDto strokeMongResponseDto = StrokeMongResponseDto.builder()
                .mongId(mong.getMongId())
                .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                .createdAt(mong.getCreatedAt())
                .updatedAt(mong.getUpdatedAt())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.STROKE_MONG.toResponseDto(strokeMongResponseDto));
    }

    /**
     * 몽 수면/기상
     */
    @EntryLoggingPoint
    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<ResponseDto<SleepWakeupMongResponseDto>> sleepMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {

        Mong mong = managementUseCase.getMongUseCase(GetMongCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build());

        if (mong.getIsSleep()) {
            WakeupMongCommand command = WakeupMongCommand.builder()
                    .accountId(passport.getAccountId())
                    .mongId(mongId)
                    .build();

            mong = managementUseCase.wakeUpMongUseCase(command);
        } else {
            SleepMongCommand command = SleepMongCommand.builder()
                    .accountId(passport.getAccountId())
                    .mongId(mongId)
                    .build();

            mong = managementUseCase.sleepMongUseCase(command);
        }

        SleepWakeupMongResponseDto sleepWakeupMongResponseDto = SleepWakeupMongResponseDto.builder()
                .mongId(mong.getMongId())
                .isSleep(mong.getIsSleep())
                .createdAt(mong.getCreatedAt())
                .updatedAt(mong.getUpdatedAt())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.SLEEP_WAKEUP_MONG.toResponseDto(sleepWakeupMongResponseDto));
    }

    /**
     * 몽 배변 처리
     */
    @EntryLoggingPoint
    @PostMapping("/poopClean/{mongId}")
    public ResponseEntity<ResponseDto<PoopCleanMongResponseDto>> poopCleanMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {

        PoopCleanMongCommand command = PoopCleanMongCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        Mong mong = managementUseCase.poopCleanMongUseCase(command);

        PoopCleanMongResponseDto poopCleanMongResponseDto = PoopCleanMongResponseDto.builder()
                .mongId(mong.getMongId())
                .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                .poopCount(mong.getPoopCount())
                .createdAt(mong.getCreatedAt())
                .updatedAt(mong.getUpdatedAt())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.POOP_CLEAN_MONG.toResponseDto(poopCleanMongResponseDto));
    }

    /**
     * 몽 진화
     */
    @EntryLoggingPoint
    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<ResponseDto<EvolutionMongResponseDto>> evolutionMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {

        EvolutionMongCommand command = EvolutionMongCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        Mong mong = managementUseCase.evolutionMongUseCase(command);

        EvolutionMongResponseDto evolutionMongResponseDto = EvolutionMongResponseDto.builder()
                .mongId(mong.getMongId())
                .mongCode(mong.getMongCode())
                .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                .strengthRatio(mong.getStrength() / mong.getMaxStatus() * 100)
                .healthyRatio(mong.getHealthy() / mong.getMaxStatus() * 100)
                .satietyRatio(mong.getSatiety() / mong.getMaxStatus() * 100)
                .fatigueRatio(mong.getFatigue() / mong.getMaxStatus() * 100)
                .stateCode(mong.getStateCode())
                .statusCode(mong.getStatusCode())
                .createdAt(mong.getCreatedAt())
                .updatedAt(mong.getUpdatedAt())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.EVOLUTION_MONG.toResponseDto(evolutionMongResponseDto));
    }

    /**
     * 몽 졸업
     */
    @EntryLoggingPoint
    @PutMapping("/graduate/{mongId}")
    public ResponseEntity<ResponseDto<GraduateMongResponseDto>> graduateMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {

        GraduateMongCommand command = GraduateMongCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        Mong mong = managementUseCase.graduateMongUseCase(command);

        GraduateMongResponseDto graduateMongResponseDto = GraduateMongResponseDto.builder()
                .mongId(mong.getMongId())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.GRADUATE_MONG.toResponseDto(graduateMongResponseDto));
    }
}
