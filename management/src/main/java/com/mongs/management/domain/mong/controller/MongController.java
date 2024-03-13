package com.mongs.management.domain.mong.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.management.domain.mong.client.LifecycleClient;
import com.mongs.management.domain.mong.service.MongService;
import com.mongs.management.domain.mong.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class MongController {

    private final MongService mongService;

    // 몽 생성
    @PostMapping("")
    public ResponseEntity<CreateMong> createMong(
            @RequestBody InitMong initMong,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        CreateMong createMong = mongService.createMong(initMong, passportDetail.getId(), passportDetail.getEmail());
        mongService.eggMong(createMong.id());

        return ResponseEntity
                .ok()
                .body(createMong);
    }

    // 쓰다듬기 (수정 필_)
    @PutMapping("/stroke/{mongId}")
    public ResponseEntity<Stroke> toMongStroke(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        return ResponseEntity
                .ok()
                .body(mongService.toMongStroke(mongId, passportDetail.getId()));
    }

    // 잠자기 토글
    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<Sleep> isMongSleep(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.toMongSleeping(mongId, passportDetail.getId(), passportDetail.getEmail()));
    }

    // 똥 치우기
    @PutMapping("/poop/{mongId}")
    public ResponseEntity<Poop> toCleanMongsPoop(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.toCleanMongsPoop(mongId, passportDetail.getId(), passportDetail.getEmail()));
    }

    @PutMapping("/feed/{mongId}")
    public ResponseEntity<EatTheFeed> feedToMong(
            @RequestBody FeedCode feed,
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.feedToMong(feed, mongId, passportDetail.getId(), passportDetail.getEmail()));
    }

    // 훈련
    @PutMapping("/training/{mongId}")
    public ResponseEntity<Training> mongTraining(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.mongTraining(mongId, passportDetail.getId()));
    }

    // 졸업
    @PutMapping("/graduation/{mongId}")
    public ResponseEntity<Graduation> mongsGraduate(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.mongsGraduate(mongId, passportDetail.getId(), passportDetail.getEmail()));

    }

    // 진화
    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<Evolution> mongEvolution(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.mongEvolution(mongId, passportDetail.getId(), passportDetail.getEmail()));
    }
}
