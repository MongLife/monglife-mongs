package com.monglife.mongs.app.user.collection.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.collection.dto.request.CreateCollectionMapRequestDto;
import com.monglife.mongs.app.user.collection.dto.request.CreateCollectionMongRequestDto;
import com.monglife.mongs.app.user.collection.dto.response.GetCollectionMapResponseDto;
import com.monglife.mongs.app.user.collection.dto.response.GetCollectionMongResponseDto;
import com.monglife.mongs.app.user.collection.enums.CollectionResponse;
import com.monglife.mongs.app.user.collection.service.CollectionService;
import com.monglife.mongs.client.manager.client.ManagementClient;
import com.monglife.mongs.domain.member.dto.etc.GetCollectionMapDto;
import com.monglife.mongs.domain.member.dto.etc.GetCollectionMongDto;
import com.monglife.mongs.module.security.global.principal.Passport;
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
     * For Test
     * TODO: 삭제 필요
     */
    private final ManagementClient managementClient;
    @GetMapping("/healthCheck")
    public String healthCheck() {
        return managementClient.healthCheck();
    }

    /**
     * 맵 컬렉션 조회
     * @param passport 패스 포트
     * @return 맵 컬렉션 목록
     */
    @GetMapping("/map")
    public ResponseEntity<ResponseDto<List<GetCollectionMapResponseDto>>> getCollectionMaps(@AuthenticationPrincipal Passport passport) {

        Long accountId = passport.getAccountId();

        List<GetCollectionMapDto> getCollectionMapDtos = collectionService.getCollectionMaps(accountId);

        List<GetCollectionMapResponseDto> getCollectionMapResponseDtos = getCollectionMapDtos.stream()
                .map(getCollectionMapDto -> GetCollectionMapResponseDto.builder()
                        .mapTypeCode(getCollectionMapDto.getMapTypeCode())
                        .mapTypeName(getCollectionMapDto.getMapTypeName())
                        .isIncluded(getCollectionMapDto.getIsIncluded())
                        .build())
                .toList();

        return ResponseEntity.ok(CollectionResponse.APP_USER_COLLECTION_GET_COLLECTION_MAP.toResponseDto(getCollectionMapResponseDtos));
    }

    @GetMapping("/mong")
    public ResponseEntity<ResponseDto<List<GetCollectionMongResponseDto>>> getCollectionMongs(@AuthenticationPrincipal Passport passport) {

        Long accountId = passport.getAccountId();

        List<GetCollectionMongDto> getCollectionMongDtos = collectionService.getCollectionMongs(accountId);

        List<GetCollectionMongResponseDto> getCollectionMongResponseDtos = getCollectionMongDtos.stream()
                .map(getCollectionMongDto -> GetCollectionMongResponseDto.builder()
                        .mongTypeCode(getCollectionMongDto.getMongTypeCode())
                        .mongTypeName(getCollectionMongDto.getMongTypeName())
                        .isIncluded(getCollectionMongDto.getIsIncluded())
                        .build())
                .toList();

        return ResponseEntity.ok(CollectionResponse.APP_USER_COLLECTION_GET_COLLECTION_MONG.toResponseDto(getCollectionMongResponseDtos));
    }
}
