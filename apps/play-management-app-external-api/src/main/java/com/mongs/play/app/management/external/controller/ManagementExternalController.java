package com.mongs.play.app.management.external.controller;

import com.mongs.play.app.management.external.dto.req.FeedMongReqDto;
import com.mongs.play.app.management.external.dto.req.RegisterMongReqDto;
import com.mongs.play.app.management.external.dto.req.TrainingMongReqDto;
import com.mongs.play.app.management.external.dto.req.ValidationTrainingMongReqDto;
import com.mongs.play.app.management.external.dto.res.*;
import com.mongs.play.app.management.external.service.ManagementExternalService;
import com.mongs.play.app.management.external.vo.*;
import com.mongs.play.module.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/management")
@RequiredArgsConstructor
@RestController
public class ManagementExternalController {

    private final ManagementExternalService managementExternalService;

    @GetMapping("")
    public ResponseEntity<List<FindMongResDto>> findMong(
            @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        List<FindMongVo> findMongVoList = managementExternalService.findMong(accountId);

        return ResponseEntity.ok().body(findMongVoList.stream()
                .map(findMongVo -> FindMongResDto.builder()
                        .mongId(findMongVo.mongId())
                        .name(findMongVo.name())
                        .mongCode(findMongVo.mongCode())
                        .weight(findMongVo.weight())
                        .strength(findMongVo.strength())
                        .satiety(findMongVo.satiety())
                        .healthy(findMongVo.healthy())
                        .sleep(findMongVo.sleep())
                        .exp(findMongVo.exp())
                        .poopCount(findMongVo.poopCount())
                        .isSleeping(findMongVo.isSleeping())
                        .stateCode(findMongVo.state().code)
                        .shiftCode(findMongVo.shift().code)
                        .payPoint(findMongVo.payPoint())
                        .born(findMongVo.born())
                        .build())
                .toList());
    }

    @PostMapping("")
    public ResponseEntity<RegisterMongResDto> registerMong(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody RegisterMongReqDto registerMongReqDto) {

        Long accountId = passportDetail.getId();
        String name = registerMongReqDto.name();
        String sleepStart = registerMongReqDto.sleepStart();
        String sleepEnd = registerMongReqDto.sleepEnd();

        RegisterMongVo registerMongVo =
                managementExternalService.registerMong(accountId, name, sleepStart, sleepEnd);

        return ResponseEntity.ok().body(RegisterMongResDto.builder()
                .mongId(registerMongVo.mongId())
                .name(registerMongVo.name())
                .mongCode(registerMongVo.mongCode())
                .weight(registerMongVo.weight())
                .strength(registerMongVo.strength())
                .satiety(registerMongVo.satiety())
                .healthy(registerMongVo.healthy())
                .sleep(registerMongVo.sleep())
                .exp(registerMongVo.exp())
                .poopCount(registerMongVo.poopCount())
                .isSleeping(registerMongVo.isSleeping())
                .stateCode(registerMongVo.state().code)
                .shiftCode(registerMongVo.shift().code)
                .payPoint(registerMongVo.payPoint())
                .born(registerMongVo.born())
                .build());
    }

    @DeleteMapping("/{mongId}")
    public ResponseEntity<DeleteMongResDto> deleteMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        DeleteMongVo deleteMongVo = managementExternalService.deleteMong(accountId, mongId);

        return ResponseEntity.ok().body(DeleteMongResDto.builder()
                .mongId(deleteMongVo.mongId())
                .shiftCode(deleteMongVo.shift().code)
                .build());
    }

    @PutMapping("/stroke/{mongId}")
    public ResponseEntity<StrokeMongResDto> strokeMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        StrokeMongVo strokeMongVo = managementExternalService.strokeMong(accountId, mongId);

        return ResponseEntity.ok().body(StrokeMongResDto.builder()
                .mongId(strokeMongVo.mongId())
                .exp(strokeMongVo.exp())
                .build());
    }

    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<SleepingMongResDto> sleepingMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        SleepingMongVo sleepingMongVo = managementExternalService.sleepingMong(accountId, mongId);

        return ResponseEntity.ok().body(SleepingMongResDto.builder()
                .mongId(sleepingMongVo.mongId())
                .isSleeping(sleepingMongVo.isSleeping())
                .build());
    }

    @PutMapping("/poopClean/{mongId}")
    public ResponseEntity<PoopCleanMongResDto> poopClean(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        PoopCleanMongVo poopCleanMongVo = managementExternalService.poopClean(accountId, mongId);

        return ResponseEntity.ok().body(PoopCleanMongResDto.builder()
                .mongId(poopCleanMongVo.mongId())
                .poopCount(poopCleanMongVo.poopCount())
                .exp(poopCleanMongVo.exp())
                .build());
    }

    @GetMapping("/validationTraining/{mongId}")
    public ResponseEntity<ValidationTrainingMongVo> validationTrainingMong(
            @PathVariable("mongId") Long mongId, @RequestParam("trainingCode") String trainingCode) {

        ValidationTrainingMongVo validationTrainingMongVo = managementExternalService.validationTrainingMong(mongId, trainingCode);

        return ResponseEntity.ok().body(ValidationTrainingMongVo.builder()
                .mongId(validationTrainingMongVo.mongId())
                .isPossible(validationTrainingMongVo.isPossible())
                .build());
    }

    @PutMapping("/training/{mongId}")
    public ResponseEntity<TrainingMongResDto> trainingMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody TrainingMongReqDto trainingMongReqDto) {

        Long accountId = passportDetail.getId();
        String trainingCode = trainingMongReqDto.trainingCode();

        TrainingMongVo trainingMongVo = managementExternalService.trainingMong(accountId, mongId, trainingCode);

        return ResponseEntity.ok().body(TrainingMongResDto.builder()
                .mongId(trainingMongVo.mongId())
                .strength(trainingMongVo.strength())
                .exp(trainingMongVo.exp())
                .payPoint(trainingMongVo.payPoint())
                .build());
    }

    @PutMapping("/graduate/{mongId}")
    public ResponseEntity<GraduateMongResDto> graduateMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        GraduateMongVo graduateMongVo = managementExternalService.graduateMong(accountId, mongId);

        return ResponseEntity.ok().body(GraduateMongResDto.builder()
                .mongId(graduateMongVo.mongId())
                .shiftCode(graduateMongVo.shift().code)
                .build());
    }

    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<EvolutionMongResDto> evolutionMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        EvolutionMongVo evolutionMongVo = managementExternalService.evolutionMong(accountId, mongId);

        return ResponseEntity.ok().body(EvolutionMongResDto.builder()
                .mongId(evolutionMongVo.mongId())
                .mongCode(evolutionMongVo.mongCode())
                .weight(evolutionMongVo.weight())
                .strength(evolutionMongVo.strength())
                .satiety(evolutionMongVo.satiety())
                .healthy(evolutionMongVo.healthy())
                .sleep(evolutionMongVo.sleep())
                .exp(evolutionMongVo.exp())
                .stateCode(evolutionMongVo.state().code)
                .shiftCode(evolutionMongVo.shift().code)
                .build());
    }

    @GetMapping("/feed/{mongId}")
    public ResponseEntity<List<FindFeedLogResDto>> findFeedLog(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail
    ) {

        Long accountId = passportDetail.getId();

        List<FindFeedLogVo> findFeedLogVoList = managementExternalService.findFeedLog(accountId, mongId);

        return ResponseEntity.ok().body(findFeedLogVoList.stream()
                .map(findFeedLogVo -> FindFeedLogResDto.builder()
                        .mongId(findFeedLogVo.mongId())
                        .code(findFeedLogVo.code())
                        .isCanBuy(findFeedLogVo.isCanBuy())
                        .build())
                .toList());
    }

    @PostMapping("/feed/{mongId}")
    public ResponseEntity<FeedMongResDto> feedMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody FeedMongReqDto feedMongReqDto
    ) {

        Long accountId = passportDetail.getId();
        String foodCode = feedMongReqDto.foodCode();

        FeedMongVo feedMongVo = managementExternalService.feedMong(accountId, mongId, foodCode);

        return ResponseEntity.ok().body(FeedMongResDto.builder()
                .mongId(feedMongVo.mongId())
                .weight(feedMongVo.weight())
                .strength(feedMongVo.strength())
                .satiety(feedMongVo.satiety())
                .healthy(feedMongVo.healthy())
                .sleep(feedMongVo.sleep())
                .exp(feedMongVo.exp())
                .payPoint(feedMongVo.payPoint())
                .build());
    }
}
