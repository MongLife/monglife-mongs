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
@RequestMapping("/collection/internal")
@RequiredArgsConstructor
public class CollectionInternalController {

    private final CollectionService collectionService;

    /**
     * For Test
     * TODO: 삭제 필요
     */
    @GetMapping("/health")
    public String health() {
        return "manager health";
    }

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
