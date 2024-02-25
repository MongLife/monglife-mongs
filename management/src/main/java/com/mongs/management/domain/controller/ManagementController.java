package com.mongs.management.domain.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.management.domain.service.ManagementService;
import com.mongs.management.domain.service.dto.*;
import com.mongs.management.response.ResponseHandler;
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
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal PassportDetail passportDetail) {
        // passport : 인증 객체 (gateway 로 부터 넘어오는 사용자 정보 값)
        return passportDetail.toString();
    }

    @PostMapping("/")
    public ResponseEntity<ResponseHandler<CreateMong>> createMong(@RequestBody InitMong initMong,
                                                                  @AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .ok()
                .body(ResponseHandler.<CreateMong>builder()
                        .message("몽 생성")
                        .statusCode(HttpStatus.CREATED)
                        .data(managementService.createMong(initMong, passportDetail.getId()))
                        .build());
    }

    @PutMapping("/stroke")
    public ResponseEntity<ResponseHandler<Stroke>> toMongStroke(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .ok()
                .body(ResponseHandler.<Stroke>builder()
                        .message("몽 쓰다듬기 성공")
                        .statusCode(HttpStatus.ACCEPTED)
                        .data(managementService.toMongStroke(passportDetail.getId()))
                        .build());
    }

    @PutMapping("/poop")
    public ResponseEntity<ResponseHandler<Poop>> toCleanMongsPoop(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(ResponseHandler.<Poop>builder()
                        .message("몽 배변 치우기")
                        .statusCode(HttpStatus.ACCEPTED)
                        .data(managementService.toCleanMongsPoop(passportDetail.getId()))
                        .build());
    }

    @PutMapping("/training")
    public ResponseEntity<ResponseHandler<Training>> mongTraining(@RequestBody TrainingCount trainingCount, @AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(ResponseHandler.<Training>builder()
                        .message("몽 훈련")
                        .statusCode(HttpStatus.ACCEPTED)
                        .data(managementService.mongTraining(trainingCount,passportDetail.getId()))
                        .build());
    }

    @PutMapping("/sleep/toggle")
    public ResponseEntity<ResponseHandler<Sleep>> isMongSleep(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(ResponseHandler.<Sleep>builder()
                        .message("몽이 자는지 확인")
                        .statusCode(HttpStatus.ACCEPTED)
                        .data(managementService.toCheckMongsLifetime(passportDetail.getId()))
                        .build());
    }
}
