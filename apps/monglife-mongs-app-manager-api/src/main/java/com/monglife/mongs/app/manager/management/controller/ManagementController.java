package com.monglife.mongs.app.manager.management.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.manager.global.enums.ManagerResponse;
import com.monglife.mongs.app.manager.management.dto.etc.GetFeedItemDto;
import com.monglife.mongs.app.manager.management.dto.etc.GetMongDto;
import com.monglife.mongs.app.manager.management.dto.request.CreateMongRequestDto;
import com.monglife.mongs.app.manager.management.dto.request.FeedMongRequestDto;
import com.monglife.mongs.app.manager.management.dto.response.GetFeedItemsResponseDto;
import com.monglife.mongs.app.manager.management.dto.response.GetMongResponseDto;
import com.monglife.mongs.app.manager.management.service.ManagementService;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/management")
public class ManagementController {

    private final ManagementService managementService;


    /**
     * 몽 목록 조회
     * @param passport 패스 포트
     * @return 몽 목록 응답 DTO
     */
    @GetMapping("")
    public ResponseEntity<ResponseDto<List<GetMongResponseDto>>> getMongs(@AuthenticationPrincipal Passport passport) {

        Long accountId = passport.getId();

        List<GetMongDto> getMongDtos = managementService.getMongs(accountId);

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
    public ResponseEntity<ResponseDto<GetMongResponseDto>> getMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {

        Long accountId = passport.getId();

        GetMongDto getMongDto = managementService.getMong(accountId, mongId);

        GetMongResponseDto getMongResponseDto = GetMongResponseDto.of(getMongDto);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_GET_MONG.toResponseDto(getMongResponseDto));
    }

    /**
     * 음식/간식 목록 조회
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @param foodTypeGroupCode 음식/간식 그룹 코드
     * @return 음식/간식 목록 조회 응답 DTO
     */
    // TODO: 음식 로그 조회
    @GetMapping("/feed/{foodTypeGroupCode}/{mongId}")
    public ResponseEntity<ResponseDto<GetFeedItemsResponseDto>> getFeedItems(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId, @PathVariable("foodTypeGroupCode") String foodTypeGroupCode) {

        Long accountId = passport.getId();

        List<GetFeedItemDto> getFeedItemDtos = managementService.getFeedItems(accountId, mongId, foodTypeGroupCode);

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

        Long accountId = passport.getId();
        String name = createMongRequestDto.getName();
        LocalTime sleepAt = createMongRequestDto.getSleepAt();
        LocalTime wakeupAt = createMongRequestDto.getWakeupAt();

        managementService.createMong(accountId, name, sleepAt, wakeupAt);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_CREATE_MONG.toResponseDto());
    }

    /**
     * 몽 삭제
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 삭제 응답 코드
     */
    @DeleteMapping("/{mongId}")
    public ResponseEntity<ResponseDto<?>> deleteMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {

        Long accountId = passport.getId();

        managementService.deleteMong(accountId, mongId);

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
    public ResponseEntity<ResponseDto<?>> feedMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId, @RequestBody FeedMongRequestDto feedMongRequestDto) {

        Long accountId = passport.getId();
        String foodTypeCode = feedMongRequestDto.getFoodTypeCode();

        managementService.feedMong(accountId, mongId, foodTypeCode);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_FEED_MONG.toResponseDto());
    }

    /**
     * 몽 쓰다 듬기
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 쓰다 듬기 응답 코드
     */
    @PostMapping("/stroke/{mongId}")
    public ResponseEntity<ResponseDto<?>> strokeMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {

        Long accountId = passport.getId();

        managementService.strokeMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_STROKE_MONG.toResponseDto());
    }

    /**
     * 몽 수면/기상
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 수면/기상 응답 코드
     */
    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<ResponseDto<?>> sleepMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {

        Long accountId = passport.getId();

        managementService.sleepMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_SLEEP_MONG.toResponseDto());
    }

    /**
     * 몽 배변 처리
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 배변 처리 응답 코드
     */
    @PostMapping("/poopClean/{mongId}")
    public ResponseEntity<ResponseDto<?>> poopCleanMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {

        Long accountId = passport.getId();

        managementService.poopCleanMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_POOP_CLEAN_MONG.toResponseDto());
    }

    /**
     * 몽 진화
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 진화 응답 코드
     */
    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<ResponseDto<?>> evolutionMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {

        Long accountId = passport.getId();

        managementService.evolutionMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_EVOLUTION_MONG.toResponseDto());
    }

    /**
     * 몽 졸업
     * @param passport 패스 포트
     * @param mongId 몽 ID
     * @return 몽 졸업 응답 코드
     */
    @PutMapping("/graduate/{mongId}")
    public ResponseEntity<ResponseDto<?>> graduateMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {

        Long accountId = passport.getId();

        managementService.graduateMong(accountId, mongId);

        return ResponseEntity.ok(ManagerResponse.MANAGER_MANAGEMENT_GRADUATE_MONG.toResponseDto());
    }
}
