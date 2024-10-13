package com.monglife.mongs.app.management.presentation.controller;

import com.monglife.mongs.app.management.business.service.MongInteractionService;
import com.monglife.mongs.app.management.business.service.MongManagerService;
import com.monglife.mongs.app.management.presentation.dto.req.FeedMongReqDto;
import com.monglife.mongs.app.management.presentation.dto.req.RegisterMongReqDto;
import com.monglife.mongs.app.management.presentation.dto.req.TrainingMongReqDto;
import com.monglife.mongs.app.management.presentation.dto.res.*;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/management")
@RequiredArgsConstructor
@RestController
public class ManagementController {

    private final MongManagerService mongManagerService;
    private final MongInteractionService mongInteractionService;

    @GetMapping("")
    public ResponseEntity<List<FindMongResDto>> findMong(
            @AuthenticationPrincipal Passport passport
    ) {

        Long accountId = passport.getId();

        var voList = mongManagerService.findMong(accountId);

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
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId
    ) {

        Long accountId = passport.getId();

        var vo = mongManagerService.findMong(accountId, mongId);

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
            @AuthenticationPrincipal Passport passport,
            @RequestBody RegisterMongReqDto registerMongReqDto
    ) {

        Long accountId = passport.getId();
        String name = registerMongReqDto.name();
        String sleepStart = registerMongReqDto.sleepStart();
        String sleepEnd = registerMongReqDto.sleepEnd();

        var vo = mongManagerService.registerMong(accountId, name, sleepStart, sleepEnd);

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
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId
    ) {

        Long accountId = passport.getId();

        var vo = mongManagerService.deleteMong(accountId, mongId);

        return ResponseEntity.ok().body(DeleteMongResDto.builder()
                .mongId(vo.mongId())
                .shiftCode(vo.shiftCode())
                .build());
    }

    @PutMapping("/stroke/{mongId}")
    public ResponseEntity<StrokeMongResDto> strokeMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId
    ) {

        Long accountId = passport.getId();

        var vo = mongInteractionService.strokeMong(accountId, mongId);

        return ResponseEntity.ok().body(StrokeMongResDto.builder()
                .mongId(vo.mongId())
                .exp(vo.expPercent())
                .build());
    }

    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<SleepingMongResDto> sleepingMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId
    ) {

        Long accountId = passport.getId();

        var vo = mongInteractionService.sleepingMong(accountId, mongId);

        return ResponseEntity.ok().body(SleepingMongResDto.builder()
                .mongId(vo.mongId())
                .isSleeping(vo.isSleeping())
                .build());
    }

    @PutMapping("/poopClean/{mongId}")
    public ResponseEntity<PoopCleanMongResDto> poopClean(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId
    ) {

        Long accountId = passport.getId();

        var vo = mongInteractionService.poopClean(accountId, mongId);

        return ResponseEntity.ok().body(PoopCleanMongResDto.builder()
                .mongId(vo.mongId())
                .poopCount(vo.poopCount())
                .exp(vo.expPercent())
                .build());
    }

    @PutMapping("/training/{mongId}")
    public ResponseEntity<TrainingMongResDto> trainingMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId,
            @RequestBody TrainingMongReqDto trainingMongReqDto
    ) {

        Long accountId = passport.getId();
        String trainingCode = trainingMongReqDto.trainingCode();
        Integer score = trainingMongReqDto.score();

        var vo = mongInteractionService.trainingMong(accountId, mongId, trainingCode, score);

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
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId
    ) {

        Long accountId = passport.getId();

        var vo = mongInteractionService.graduateMong(accountId, mongId);

        return ResponseEntity.ok().body(GraduateMongResDto.builder()
                .mongId(vo.mongId())
                .shiftCode(vo.shiftCode())
                .build());
    }

    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<EvolutionMongResDto> evolutionMong(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId
    ) {

        Long accountId = passport.getId();

        var vo = mongInteractionService.evolutionMong(accountId, mongId);

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
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId,
            @RequestBody FeedMongReqDto feedMongReqDto
    ) {

        Long accountId = passport.getId();
        String foodCode = feedMongReqDto.foodCode();

        var vo = mongInteractionService.feedMong(accountId, mongId, foodCode);

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
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") Long mongId
    ) {

        Long accountId = passport.getId();

        var voList = mongInteractionService.findFeedLog(accountId, mongId);

        return ResponseEntity.ok().body(voList.stream()
                .map(vo -> FindFeedLogResDto.builder()
                        .mongId(vo.mongId())
                        .code(vo.code())
                        .isCanBuy(vo.isCanBuy())
                        .build())
                .toList());
    }
}
