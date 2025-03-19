package com.monglife.mongs.app.user.collection.controller;

import com.monglife.module.common.security.principal.Passport;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.collection.dto.request.CreateCollectionMapRequestDto;
import com.monglife.mongs.app.user.collection.dto.response.GetCollectionMapResponseDto;
import com.monglife.mongs.app.user.collection.dto.response.GetCollectionMongResponseDto;
import com.monglife.mongs.app.user.collection.enums.CollectionResponse;
import com.monglife.mongs.app.user.collection.service.CollectionService;
import com.monglife.mongs.domain.member.vo.CollectionMapVo;
import com.monglife.mongs.domain.member.vo.CollectionMongVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    /**
     * 맵 컬렉션 등록
     * @param passport 패스 포트
     * @param createCollectionMapRequestDto 맵 타입 코드
     * @return 성공 응답 Dto
     */
    @PostMapping("/map")
    public ResponseEntity<ResponseDto<?>> createCollectionMap(@AuthenticationPrincipal Passport passport, @RequestBody CreateCollectionMapRequestDto createCollectionMapRequestDto) {

        Long accountId = passport.getAccountId();
        Double latitude = createCollectionMapRequestDto.getLatitude();
        Double longitude = createCollectionMapRequestDto.getLongitude();

        collectionService.createCollectionMap(accountId, latitude, longitude);

        return ResponseEntity.ok(CollectionResponse.APP_USER_COLLECTION_CREATE_COLLECTION_MAP.toResponseDto());
    }

    /**
     * 맵 컬렉션 조회
     * @param passport 패스 포트
     * @return 맵 컬렉션 목록
     */
    @GetMapping("/map")
    public ResponseEntity<ResponseDto<List<GetCollectionMapResponseDto>>> getCollectionMaps(@AuthenticationPrincipal Passport passport) {

        Long accountId = passport.getAccountId();

        List<CollectionMapVo> collectionMapVos = collectionService.getCollectionMaps(accountId);

        List<GetCollectionMapResponseDto> getCollectionMapResponseDtos = collectionMapVos.stream()
                .map(collectionMapVo -> GetCollectionMapResponseDto.builder()
                        .mapTypeCode(collectionMapVo.getMapTypeCode())
                        .mapTypeName(collectionMapVo.getMapTypeName())
                        .isIncluded(collectionMapVo.getIsIncluded())
                        .build())
                .toList();

        return ResponseEntity.ok(CollectionResponse.APP_USER_COLLECTION_GET_COLLECTION_MAP.toResponseDto(getCollectionMapResponseDtos));
    }

    /**
     * 몽 컬렉션 조회
     * @param passport 패스 포트
     * @return 몽 컬렉션 목록
     */
    @GetMapping("/mong")
    public ResponseEntity<ResponseDto<List<GetCollectionMongResponseDto>>> getCollectionMongs(@AuthenticationPrincipal Passport passport) {

        Long accountId = passport.getAccountId();

        List<CollectionMongVo> collectionMongVos = collectionService.getCollectionMongs(accountId);

        List<GetCollectionMongResponseDto> getCollectionMongResponseDtos = collectionMongVos.stream()
                .map(collectionMongVo -> GetCollectionMongResponseDto.builder()
                        .mongTypeCode(collectionMongVo.getMongTypeCode())
                        .mongTypeName(collectionMongVo.getMongTypeName())
                        .isIncluded(collectionMongVo.getIsIncluded())
                        .build())
                .toList();

        return ResponseEntity.ok(CollectionResponse.APP_USER_COLLECTION_GET_COLLECTION_MONG.toResponseDto(getCollectionMongResponseDtos));
    }
}
