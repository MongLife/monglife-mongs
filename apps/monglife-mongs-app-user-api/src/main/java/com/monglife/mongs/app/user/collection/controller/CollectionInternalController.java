package com.monglife.mongs.app.user.collection.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.collection.dto.request.CreateCollectionMapRequestDto;
import com.monglife.mongs.app.user.collection.dto.request.CreateCollectionMongRequestDto;
import com.monglife.mongs.app.user.collection.enums.CollectionResponse;
import com.monglife.mongs.app.user.collection.service.CollectionService;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/internal/collection")
@RequiredArgsConstructor
public class CollectionInternalController {

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
        String mapTypeCode = createCollectionMapRequestDto.getMapTypeCode();

        collectionService.createCollectionMap(accountId, mapTypeCode);

        return ResponseEntity.ok(CollectionResponse.APP_USER_COLLECTION_CREATE_COLLECTION_MAP.toResponseDto());
    }

    @PostMapping("/mong")
    public ResponseEntity<ResponseDto<?>> createCollectionMong(@AuthenticationPrincipal Passport passport, @RequestBody CreateCollectionMongRequestDto createCollectionMongRequestDto) {

        Long accountId = passport.getAccountId();
        String mongTypeCode = createCollectionMongRequestDto.getMongTypeCode();

        collectionService.createCollectionMong(accountId, mongTypeCode);

        return ResponseEntity.ok(CollectionResponse.APP_USER_COLLECTION_CREATE_COLLECTION_MONG.toResponseDto());
    }
}
