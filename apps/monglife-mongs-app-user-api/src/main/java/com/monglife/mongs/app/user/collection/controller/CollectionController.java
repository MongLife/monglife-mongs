package com.monglife.mongs.app.user.collection.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.collection.dto.request.CreateCollectionMapRequestDto;
import com.monglife.mongs.app.user.collection.dto.request.CreateCollectionMongRequestDto;
import com.monglife.mongs.app.user.collection.dto.response.GetCollectionMapResponseDto;
import com.monglife.mongs.app.user.collection.dto.response.GetCollectionMongResponseDto;
import com.monglife.mongs.app.user.collection.enums.CollectionResponse;
import com.monglife.mongs.app.user.collection.service.CollectionService;
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
@RequiredArgsConstructor
@RequestMapping("/user/collection")
public class CollectionController {

    private final CollectionService collectionService;

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

        return ResponseEntity.ok(CollectionResponse.USER_COLLECTION_GET_COLLECTION_MAP.toResponseDto(getCollectionMapResponseDtos));
    }

    @PostMapping("/map")
    public ResponseEntity<ResponseDto<?>> createCollectionMap(@AuthenticationPrincipal Passport passport, @RequestBody CreateCollectionMapRequestDto createCollectionMapRequestDto) {

        Long accountId = passport.getAccountId();
        String mapTypeCode = createCollectionMapRequestDto.getMapTypeCode();

        collectionService.createCollectionMap(accountId, mapTypeCode);

        return ResponseEntity.ok(CollectionResponse.USER_COLLECTION_CREATE_COLLECTION_MAP.toResponseDto());
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

        return ResponseEntity.ok(CollectionResponse.USER_COLLECTION_GET_COLLECTION_MONG.toResponseDto(getCollectionMongResponseDtos));
    }

    @PostMapping("/mong")
    public ResponseEntity<ResponseDto<?>> createCollectionMong(@AuthenticationPrincipal Passport passport, @RequestBody CreateCollectionMongRequestDto createCollectionMongRequestDto) {

        Long accountId = passport.getAccountId();
        String mongTypeCode = createCollectionMongRequestDto.getMongTypeCode();

        collectionService.createCollectionMong(accountId, mongTypeCode);

        return ResponseEntity.ok(CollectionResponse.USER_COLLECTION_CREATE_COLLECTION_MONG.toResponseDto());
    }
}
