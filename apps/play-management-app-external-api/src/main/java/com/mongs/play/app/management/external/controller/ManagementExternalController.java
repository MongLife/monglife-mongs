package com.mongs.play.app.management.external.controller;

import com.mongs.play.app.management.external.dto.req.RegisterMongReqDto;
import com.mongs.play.app.management.external.dto.res.*;
import com.mongs.play.app.management.external.service.ManagementExternalService;
import com.mongs.play.app.management.external.vo.EvolutionMongVo;
import com.mongs.play.app.management.external.vo.FindMongVo;
import com.mongs.play.module.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/management")
@RequiredArgsConstructor
@RestController
public class ManagementExternalController {
    private final ManagementExternalService managementExternalService;

    //
//    @GetMapping("")
//    public ResponseEntity<List<FindMongResDto>> findMong(@AuthenticationPrincipal PassportDetail passportDetail) {
//        List<FindMongVo> findMongVoList =
//    }
//
//    @PostMapping("")
//    public ResponseEntity<RegisterMongResDto> registerMong(
//            @RequestBody RegisterMongReqDto registerMongReqDto,
//            @AuthenticationPrincipal PassportDetail passportDetail
//    ) {
//    }
//
//    @DeleteMapping("/{mongId}")
//    public ResponseEntity<DeleteMongResDto> deleteMong(
//            @PathVariable("mongId") Long mongId,
//            @AuthenticationPrincipal PassportDetail passportDetail
//    ) {
//
//    }
//
//    @PutMapping("/stroke/{mongId}")
//    public ResponseEntity<StrokeMongResDto> strokeMong(
//            @PathVariable("mongId") Long mongId,
//            @AuthenticationPrincipal PassportDetail passportDetail
//    ) {
//
//    }
//
//    @PutMapping("/sleep/{mongId}")
//    public ResponseEntity<SleepingMongResDto> sleepingMong(
//            @PathVariable("mongId") Long mongId,
//            @AuthenticationPrincipal PassportDetail passportDetail
//    ) {
//
//    }
//
//    @PutMapping("/poopClean/{mongId}")
//    public ResponseEntity<PoopCleanMongResDto> poopClean(
//            @PathVariable("mongId") Long mongId,
//            @AuthenticationPrincipal PassportDetail passportDetail
//    ) {
//
//    }
//
//    @PutMapping("/training/{mongId}")
//    public ResponseEntity<TrainingMongResDto> trainingMong(
//            @PathVariable("mongId") Long mongId,
//            @AuthenticationPrincipal PassportDetail passportDetail
//    ) {
//
//    }
//
//    @PutMapping("/graduate/{mongId}")
//    public ResponseEntity<GraduateMongResDto> graduateMong(
//            @PathVariable("mongId") Long mongId,
//            @AuthenticationPrincipal PassportDetail passportDetail
//    ) {
//
//    }
//
    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<EvolutionMongResDto> evolutionMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        EvolutionMongVo evolutionMongVo = managementExternalService.evolutionMong(1L, mongId);

        return ResponseEntity.ok().body(EvolutionMongResDto.builder()
                .build());
    }
}
