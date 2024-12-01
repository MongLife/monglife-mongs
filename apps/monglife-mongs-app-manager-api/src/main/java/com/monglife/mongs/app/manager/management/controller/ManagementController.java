package com.monglife.mongs.app.manager.management.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.manager.management.dto.request.CreateMongRequestDto;
import com.monglife.mongs.app.manager.management.dto.request.FeedMongRequestDto;
import com.monglife.mongs.app.manager.management.dto.response.GetFeedItemsResponseDto;
import com.monglife.mongs.app.manager.management.dto.response.GetMongResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagerResponse;
import com.monglife.mongs.domain.mong.dto.etc.GetFeedItemDto;
import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.module.security.global.principal.Passport;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/management")
public class ManagementController {

    private final MongService mongService;


    /**
     * 몽 목록 조회
     * @param passport 패스 포트
     * @return 몽 목록 응답 DTO
     */
    @GetMapping("")
    public ResponseEntity<ResponseDto<List<GetMongResponseDto>>> getMongs(@AuthenticationPrincipal Passport passport) {

        Long accountId = passport.getAccountId();

        List<GetMongDto> getMongDtos = mongService.getMongs(accountId);

        List<GetMongResponseDto> getMongResponseDtos = GetMongResponseDto.toList(getMongDtos);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_GET_MONG.toResponseDto(getMongResponseDtos));
    }

    /**
     * 몽 단건 조회
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 조회 응답 DTO
     */
    @GetMapping("/{mongId}")
    public ResponseEntity<ResponseDto<GetMongResponseDto>> getMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1) Long mongId) {

        Long accountId = passport.getAccountId();

        GetMongDto getMongDto = mongService.getMong(accountId, mongId);

        GetMongResponseDto getMongResponseDto = GetMongResponseDto.of(getMongDto);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_GET_MONG.toResponseDto(getMongResponseDto));
    }

    /**
     * 먹이 목록 조회
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @param foodTypeGroupCode 음식 or 간식 그룹 코드
     * @return 먹이 목록 조회 응답 DTO
     */
    @GetMapping("/feed/{mongId}")
    public ResponseEntity<ResponseDto<GetFeedItemsResponseDto>> getFeedItems(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId,
            @RequestParam("foodTypeGroupCode") @NotBlank String foodTypeGroupCode
    ) {

        Long accountId = passport.getAccountId();

        List<GetFeedItemDto> getFeedItemDtos = mongService.getFeedItems(accountId, mongId, foodTypeGroupCode);

        GetFeedItemsResponseDto getFeedItemsResponseDto = GetFeedItemsResponseDto.builder()
                .mongId(mongId)
                .feedItems(getFeedItemDtos)
                .build();

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_GET_FEED_ITEM.toResponseDto(getFeedItemsResponseDto));
    }

    /**
     * 몽 생성
     * @param passport 패스 포트
     * @param createMongRequestDto 몽 생성 요청 DTO
     * @return 몽 생성 응답 코드
     */
    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createMong(@AuthenticationPrincipal Passport passport, @RequestBody CreateMongRequestDto createMongRequestDto) {

        Long accountId = passport.getAccountId();
        String name = createMongRequestDto.getName();
        LocalTime sleepAt = createMongRequestDto.getSleepAt();
        LocalTime wakeupAt = createMongRequestDto.getWakeupAt();

        mongService.createMong(accountId, name, sleepAt, wakeupAt);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_CREATE_MONG.toResponseDto());
    }

    /**
     * 몽 삭제
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 삭제 응답 코드
     */
    @DeleteMapping("/{mongId}")
    public ResponseEntity<ResponseDto<?>> deleteMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1) Long mongId) {

        Long accountId = passport.getAccountId();

        mongService.deleteMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_DELETE_MONG.toResponseDto());
    }

    /**
     * 몽 먹이 주기
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @param feedMongRequestDto 몽 먹이 주기 관련 요청 DTO (음식/간식 타입 코드)
     * @return 몽 먹이 주기 응답 코드
     */
    @PostMapping("/feed/{mongId}")
    public ResponseEntity<ResponseDto<?>> feedMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1) Long mongId, @RequestBody FeedMongRequestDto feedMongRequestDto) {

        Long accountId = passport.getAccountId();
        String foodTypeCode = feedMongRequestDto.getFoodTypeCode();

        mongService.feedMong(accountId, mongId, foodTypeCode);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_FEED_MONG.toResponseDto());
    }

    /**
     * 몽 쓰다 듬기
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 쓰다 듬기 응답 코드
     */
    @PostMapping("/stroke/{mongId}")
    public ResponseEntity<ResponseDto<?>> strokeMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1) Long mongId) {

        Long accountId = passport.getAccountId();

        mongService.strokeMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_STROKE_MONG.toResponseDto());
    }

    /**
     * 몽 수면/기상
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 수면/기상 응답 코드
     */
    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<ResponseDto<?>> sleepMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1) Long mongId) {

        Long accountId = passport.getAccountId();

        mongService.sleepMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_SLEEP_MONG.toResponseDto());
    }

    /**
     * 몽 배변 처리
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 배변 처리 응답 코드
     */
    @PostMapping("/poopClean/{mongId}")
    public ResponseEntity<ResponseDto<?>> poopCleanMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1) Long mongId) {

        Long accountId = passport.getAccountId();

        mongService.poopCleanMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_POOP_CLEAN_MONG.toResponseDto());
    }

    /**
     * 몽 진화
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 진화 응답 코드
     */
    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<ResponseDto<?>> evolutionMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1) Long mongId) {

        Long accountId = passport.getAccountId();

        mongService.evolutionMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_EVOLUTION_MONG.toResponseDto());
    }

    /**
     * 몽 졸업
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 졸업 응답 코드
     */
    @PutMapping("/graduate/{mongId}")
    public ResponseEntity<ResponseDto<?>> graduateMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") @NotNull @Min(1) Long mongId) {

        Long accountId = passport.getAccountId();

        mongService.graduateMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_GRADUATE_MONG.toResponseDto());
    }
}
