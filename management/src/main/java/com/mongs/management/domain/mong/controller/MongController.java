package com.mongs.management.domain.mong.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.management.domain.mong.controller.dto.request.FeedMongReqDto;
import com.mongs.management.domain.mong.controller.dto.request.RegisterMongReqDto;
import com.mongs.management.domain.mong.controller.dto.response.*;
import com.mongs.management.domain.mong.service.LifecycleService;
import com.mongs.management.domain.mong.service.MongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class MongController {

    private final MongService mongService;
    private final LifecycleService lifecycleService;

    @GetMapping("")
    public ResponseEntity<List<FindMongResDto>> findAllMong(@AuthenticationPrincipal PassportDetail passportDetail) {
        Long accountId = passportDetail.getId();

        return ResponseEntity.ok().body(mongService.findAllMong(accountId));
    }

    @PostMapping("")
    public ResponseEntity<RegisterMongResDto> registerMong(
            @RequestBody RegisterMongReqDto registerMongReqDto,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        String name = registerMongReqDto.name();
        String sleepStart = registerMongReqDto.sleepStart();
        String sleepEnd = registerMongReqDto.sleepEnd();

        RegisterMongResDto registerMongResDto = mongService.registerMong(accountId, name, sleepStart, sleepEnd);
//        lifecycleService.eggMongEvent(registerMongResDto.mongId());

        return ResponseEntity.ok().body(registerMongResDto);
    }

    @DeleteMapping("/{mongId}")
    public ResponseEntity<DeleteMongResDto> deleteMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        return ResponseEntity.ok().body(mongService.deleteMong(accountId, mongId));
    }

    @PutMapping("/stroke/{mongId}")
    public ResponseEntity<StrokeMongResDto> strokeMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        return ResponseEntity.ok().body(mongService.strokeMong(accountId, mongId));
    }

    @PutMapping("/feed/{mongId}")
    public ResponseEntity<FeedMongResDto> feedMong(
            @RequestBody FeedMongReqDto feedMongReqDto,
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        String feedCode = feedMongReqDto.feedCode();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mongService.feedMong(accountId, mongId, feedCode));
    }

    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<SleepMongResDto> sleepMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mongService.sleepMong(accountId, mongId));
    }

    @PutMapping("/poop/{mongId}")
    public ResponseEntity<PoopCleanResDto> poopClean(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mongService.poopClean(accountId, mongId));
    }

    @PutMapping("/training/{mongId}")
    public ResponseEntity<TrainingMongResDto> trainingMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mongService.trainingMong(accountId, mongId));
    }

    // 아에 졸업 시키고 삭제 시키는 함수
    @PutMapping("/graduation/{mongId}")
    public ResponseEntity<GraduateMongResDto> graduateMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mongService.graduateMong(accountId, mongId));

    }

    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<EvolutionMongResDto> evolutionMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mongService.evolutionMong(accountId, mongId));
    }
}
