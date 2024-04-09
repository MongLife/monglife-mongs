package com.mongs.management.domain.management.controller;

import com.mongs.core.enums.management.MongTraining;
import com.mongs.core.security.principal.PassportDetail;
import com.mongs.management.domain.management.controller.dto.request.RegisterMongReqDto;
import com.mongs.management.domain.management.controller.dto.response.*;
import com.mongs.management.domain.management.service.ManagementService;
import com.mongs.management.domain.management.service.vo.MongVo;
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
public class ManagementController {

    private final ManagementService managementService;

    /**
     * 모든 몽 조회
     * 자신이 가진 모든 몽을 조회한다.
     *
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link FindMongResDto}
     */
    @GetMapping("")
    public ResponseEntity<List<FindMongResDto>> findAllMong(@AuthenticationPrincipal PassportDetail passportDetail) {
        Long accountId = passportDetail.getId();

        List<MongVo> mongVoList = managementService.findAllMong(accountId);

        return ResponseEntity.ok().body(FindMongResDto.toList(mongVoList));
    }

    /**
     * 몽 등록
     * 새로운 몽을 등록한다.
     *
     * @param registerMongReqDto 몽 이름, 정기 수면 시작 시간(HH:mm), 정기 수면 종료 시간(HH:mm)
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link RegisterMongResDto}
     */
    @PostMapping("")
    public ResponseEntity<RegisterMongResDto> registerMong(
            @RequestBody RegisterMongReqDto registerMongReqDto,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        String name = registerMongReqDto.name();
        String sleepStart = registerMongReqDto.sleepStart();
        String sleepEnd = registerMongReqDto.sleepEnd();

        MongVo mongVo = managementService.registerMong(accountId, name, sleepStart, sleepEnd);

        return ResponseEntity.ok().body(RegisterMongResDto.of(mongVo));
    }

    /**
     * 몽 삭제
     * mongId 에 해당하는 몽을 삭제한다.
     *
     * @param mongId 몽 Id
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link DeleteMongResDto}
     */
    @DeleteMapping("/{mongId}")
    public ResponseEntity<DeleteMongResDto> deleteMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        MongVo mongVo = managementService.deleteMong(accountId, mongId);

        return ResponseEntity.ok().body(DeleteMongResDto.of(mongVo));
    }

    /**
     * 몽 쓰다듬기
     * mongId 에 해당하는 몽을 쓰다듬는다.
     *
     * @param mongId 몽 Id
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link StrokeMongResDto}
     */
    @PutMapping("/stroke/{mongId}")
    public ResponseEntity<StrokeMongResDto> strokeMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        MongVo mongVo = managementService.strokeMong(accountId, mongId);

        return ResponseEntity.ok().body(StrokeMongResDto.of(mongVo));
    }

    /**
     * 몽 수면
     * mongId 에 해당하는 몽을 재우거나 깨운다.
     * 수면 상태이면 깨우고, 비수면 상태이면 재운다.
     *
     * @param mongId 몽 Id
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link SleepingMongResDto}
     */
    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<SleepingMongResDto> sleepingMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        MongVo mongVo = managementService.sleepingMong(accountId, mongId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(SleepingMongResDto.of(mongVo));
    }

    /**
     * 몽 배변 치우기
     * mongId 에 해당하는 몽의 배변을 치운다.
     *
     * @param mongId 몽 Id
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link PoopCleanResDto}
     */
    @PutMapping("/poop/{mongId}")
    public ResponseEntity<PoopCleanResDto> poopClean(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        MongVo mongVo = managementService.poopClean(accountId, mongId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(PoopCleanResDto.of(mongVo));
    }

    /**
     * 몽 훈련
     * mongId 에 해당하는 몽의 훈련 지수를 처리한다.
     *
     * @param mongId 몽 Id
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link TrainingMongResDto}
     */
    @PutMapping("/training/{mongId}")
    public ResponseEntity<TrainingMongResDto> trainingMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        MongVo mongVo = managementService.trainingMong(accountId, mongId, MongTraining.JUMPING);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(TrainingMongResDto.of(mongVo));
    }

    /**
     * 몽 졸업
     * mongId 에 해당하는 몽을 졸업 시키고, 비활성화한다.
     *
     * @param mongId 몽 Id
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link GraduateMongResDto}
     */
    @PutMapping("/graduation/{mongId}")
    public ResponseEntity<GraduateMongResDto> graduateMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        MongVo mongVo = managementService.graduateMong(accountId, mongId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(GraduateMongResDto.of(mongVo));

    }

    /**
     * 몽 진화
     * mongId 에 해당하는 몽을 진화시킨다.
     *
     * @param mongId 몽 Id
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link EvolutionMongResDto}
     */
    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<EvolutionMongResDto> evolutionMong(
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        MongVo mongVo = managementService.evolutionMong(accountId, mongId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(EvolutionMongResDto.of(mongVo));
    }
}
