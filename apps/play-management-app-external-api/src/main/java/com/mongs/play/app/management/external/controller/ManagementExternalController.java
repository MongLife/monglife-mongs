package com.mongs.play.app.management.external.controller;

import com.mongs.play.app.management.external.dto.req.FeedMongReqDto;
import com.mongs.play.app.management.external.dto.req.RegisterMongReqDto;
import com.mongs.play.app.management.external.dto.req.TrainingMongReqDto;
import com.mongs.play.app.management.external.dto.res.*;
import com.mongs.play.app.management.external.service.ManagementExternalService;
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

        var voList = managementExternalService.findMong(accountId);

        return ResponseEntity.ok().body(voList.stream()
                .map(vo -> FindMongResDto.builder()
                        .mongId(vo.mongId())
                        .name(vo.name())
                        .mongCode(vo.mongCode())
                        .weight(vo.weight())
                        .strength(vo.strengthPercent())
                        .satiety(vo.satietyPercent())
                        .healthy(vo.healthyPercent())
                        .sleep(vo.sleepPercent())
                        .exp(vo.expPercent())
                        .poopCount(vo.poopCount())
                        .isSleeping(vo.isSleeping())
                        .stateCode(vo.stateCode())
                        .shiftCode(vo.shiftCode())
                        .payPoint(vo.payPoint())
                        .born(vo.born())
                        .build())
                .toList());
    }

    @GetMapping("/{mongId}")
    public ResponseEntity<FindMongResDto> findMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        var vo = managementExternalService.findMong(accountId, mongId);

        return ResponseEntity.ok().body(FindMongResDto.builder()
                .mongId(vo.mongId())
                .name(vo.name())
                .mongCode(vo.mongCode())
                .weight(vo.weight())
                .strength(vo.strengthPercent())
                .satiety(vo.satietyPercent())
                .healthy(vo.healthyPercent())
                .sleep(vo.sleepPercent())
                .exp(vo.expPercent())
                .poopCount(vo.poopCount())
                .isSleeping(vo.isSleeping())
                .stateCode(vo.stateCode())
                .shiftCode(vo.shiftCode())
                .payPoint(vo.payPoint())
                .born(vo.born())
                .build());
    }

    @PostMapping("")
    public ResponseEntity<RegisterMongResDto> registerMong(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody RegisterMongReqDto registerMongReqDto) {

        Long accountId = passportDetail.getId();
        String name = registerMongReqDto.name();
        String sleepStart = registerMongReqDto.sleepStart();
        String sleepEnd = registerMongReqDto.sleepEnd();

        var vo = managementExternalService.registerMong(accountId, name, sleepStart, sleepEnd);

        return ResponseEntity.ok().body(RegisterMongResDto.builder()
                .mongId(vo.mongId())
                .name(vo.name())
                .mongCode(vo.mongCode())
                .weight(vo.weight())
                .strength(vo.strengthPercent())
                .satiety(vo.satietyPercent())
                .healthy(vo.healthyPercent())
                .sleep(vo.sleepPercent())
                .exp(vo.expPercent())
                .poopCount(vo.poopCount())
                .isSleeping(vo.isSleeping())
                .stateCode(vo.stateCode())
                .shiftCode(vo.shiftCode())
                .payPoint(vo.payPoint())
                .born(vo.born())
                .build());
    }

    @DeleteMapping("/{mongId}")
    public ResponseEntity<DeleteMongResDto> deleteMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        var vo = managementExternalService.deleteMong(accountId, mongId);

        return ResponseEntity.ok().body(DeleteMongResDto.builder()
                .mongId(vo.mongId())
                .shiftCode(vo.shiftCode())
                .build());
    }

    @PutMapping("/stroke/{mongId}")
    public ResponseEntity<StrokeMongResDto> strokeMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        var vo = managementExternalService.strokeMong(accountId, mongId);

        return ResponseEntity.ok().body(StrokeMongResDto.builder()
                .mongId(vo.mongId())
                .exp(vo.expPercent())
                .build());
    }

    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<SleepingMongResDto> sleepingMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        var vo = managementExternalService.sleepingMong(accountId, mongId);

        return ResponseEntity.ok().body(SleepingMongResDto.builder()
                .mongId(vo.mongId())
                .isSleeping(vo.isSleeping())
                .build());
    }

    @PutMapping("/poopClean/{mongId}")
    public ResponseEntity<PoopCleanMongResDto> poopClean(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        var vo = managementExternalService.poopClean(accountId, mongId);

        return ResponseEntity.ok().body(PoopCleanMongResDto.builder()
                .mongId(vo.mongId())
                .poopCount(vo.poopCount())
                .exp(vo.expPercent())
                .build());
    }

    @GetMapping("/validationTraining/{mongId}")
    public ResponseEntity<ValidationTrainingMongResDto> validationTrainingMong(
            @PathVariable("mongId") Long mongId, @RequestParam("trainingCode") String trainingCode) {

        var vo = managementExternalService.validationTrainingMong(mongId, trainingCode);

        return ResponseEntity.ok().body(ValidationTrainingMongResDto.builder()
                .mongId(vo.mongId())
                .isPossible(vo.isPossible())
                .build());
    }

    @PutMapping("/training/{mongId}")
    public ResponseEntity<TrainingMongResDto> trainingMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody TrainingMongReqDto trainingMongReqDto) {

        Long accountId = passportDetail.getId();
        String trainingCode = trainingMongReqDto.trainingCode();

        var vo = managementExternalService.trainingMong(accountId, mongId, trainingCode);

        return ResponseEntity.ok().body(TrainingMongResDto.builder()
                .mongId(vo.mongId())
                .weight(vo.weight())
                .strength(vo.strengthPercent())
                .satiety(vo.satietyPercent())
                .healthy(vo.healthyPercent())
                .sleep(vo.sleepPercent())
                .exp(vo.expPercent())
                .payPoint(vo.payPoint())
                .build());
    }

    @PutMapping("/graduate/{mongId}")
    public ResponseEntity<GraduateMongResDto> graduateMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        var vo = managementExternalService.graduateMong(accountId, mongId);

        return ResponseEntity.ok().body(GraduateMongResDto.builder()
                .mongId(vo.mongId())
                .shiftCode(vo.shiftCode())
                .build());
    }

    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<EvolutionMongResDto> evolutionMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail) {

        Long accountId = passportDetail.getId();

        var vo = managementExternalService.evolutionMong(accountId, mongId);

        return ResponseEntity.ok().body(EvolutionMongResDto.builder()
                .mongId(vo.mongId())
                .mongCode(vo.mongCode())
                .weight(vo.weight())
                .strength(vo.strengthPercent())
                .satiety(vo.satietyPercent())
                .healthy(vo.healthyPercent())
                .sleep(vo.sleepPercent())
                .exp(vo.expPercent())
                .stateCode(vo.stateCode())
                .shiftCode(vo.shiftCode())
                .build());
    }

    @PostMapping("/feed/{mongId}")
    public ResponseEntity<FeedMongResDto> feedMong(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody FeedMongReqDto feedMongReqDto
    ) {

        Long accountId = passportDetail.getId();
        String foodCode = feedMongReqDto.foodCode();

        var vo = managementExternalService.feedMong(accountId, mongId, foodCode);

        return ResponseEntity.ok().body(FeedMongResDto.builder()
                .mongId(vo.mongId())
                .weight(vo.weight())
                .strength(vo.strengthPercent())
                .satiety(vo.satietyPercent())
                .healthy(vo.healthyPercent())
                .sleep(vo.sleepPercent())
                .exp(vo.expPercent())
                .payPoint(vo.payPoint())
                .build());
    }

    @GetMapping("/feed/{mongId}")
    public ResponseEntity<List<FindFeedLogResDto>> findFeedLog(
            @PathVariable("mongId") Long mongId, @AuthenticationPrincipal PassportDetail passportDetail
    ) {

        Long accountId = passportDetail.getId();

        var voList = managementExternalService.findFeedLog(accountId, mongId);

        return ResponseEntity.ok().body(voList.stream()
                .map(vo -> FindFeedLogResDto.builder()
                        .mongId(vo.mongId())
                        .code(vo.code())
                        .isCanBuy(vo.isCanBuy())
                        .build())
                .toList());
    }
}
