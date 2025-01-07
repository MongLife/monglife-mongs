package com.monglife.mongs.app.user.collection.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.collection.dto.response.GetCollectionMapResponseDto;
import com.monglife.mongs.app.user.collection.dto.response.GetCollectionMongResponseDto;
import com.monglife.mongs.app.user.collection.enums.CollectionResponse;
import com.monglife.mongs.app.user.collection.service.CollectionService;
import com.monglife.mongs.domain.member.vo.CollectionMapVo;
import com.monglife.mongs.domain.member.vo.CollectionMongVo;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

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
