package com.mongs.management.domain.mong.controller;

import com.mongs.core.security.principal.PassportDetail;
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

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal PassportDetail passportDetail) {
        // passport : 인증 객체 (gateway 로 부터 넘어오는 사용자 정보 값)
        return passportDetail.toString();
    }

    // 몽 생성
    @PostMapping("/")
    public ResponseEntity<CreateMong> createMong(@RequestBody InitMong initMong,
                                                 @AuthenticationPrincipal PassportDetail passportDetail) {
        log.info("들어옴");
        return ResponseEntity
                .ok()
                .body(mongService.createMong(initMong, passportDetail.getId()));
    }

    // 쓰다듬기 (수정 필_)
    @PutMapping("/stroke")
    public ResponseEntity<Stroke> toMongStroke(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .ok()
                .body(mongService.toMongStroke(passportDetail.getId()));
    }

    // 똥 치우기
    @PutMapping("/poop")
    public ResponseEntity<Poop> toCleanMongsPoop(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.toCleanMongsPoop(passportDetail.getId()));
    }

    // 훈련
    @PutMapping("/training")
    public ResponseEntity<Training> mongTraining(@RequestBody TrainingCount trainingCount, @AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.mongTraining(trainingCount, passportDetail.getId()));
    }

    // 잠자기 토글
    @PutMapping("/sleep/toggle")
    public ResponseEntity<Sleep> isMongSleep(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.toCheckMongsLifetime(passportDetail.getId()));
    }

    // 졸업
    @PutMapping("/graduation")
    public ResponseEntity<Graduation> mongsGraduate(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.mongsGraduate(passportDetail.getId()));

    }

    // 진화
    @PutMapping("/evolution")
    public ResponseEntity<Evolution> mongEvolution(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.mongEvolution(passportDetail.getId()));
    }

    @PutMapping("/feed")
    public ResponseEntity<EatTheFeed> feedToMong(@RequestBody FeedCode feed, @AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mongService.feedToMong(feed, passportDetail.getId()));
    }
}
