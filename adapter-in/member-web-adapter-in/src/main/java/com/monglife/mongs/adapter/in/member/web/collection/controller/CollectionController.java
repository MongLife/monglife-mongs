package com.monglife.mongs.adapter.in.member.web.collection.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.member.web.collection.dto.request.CreateCollectionMapRequestDto;
import com.monglife.mongs.adapter.in.member.web.collection.dto.response.GetCollectionMapResponseDto;
import com.monglife.mongs.adapter.in.member.web.collection.dto.response.GetCollectionMongResponseDto;
import com.monglife.mongs.adapter.in.member.web.enums.AdapterInWebMemberResponse;
import com.monglife.mongs.application.member.port.in.CollectionUseCase;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMapCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMapsCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMongsCommand;
import com.monglife.mongs.domain.member.model.CollectionMap;
import com.monglife.mongs.domain.member.model.CollectionMong;
import jakarta.validation.Valid;
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

    private final CollectionUseCase collectionUseCase;

    /**
     * 맵 컬렉션 등록
     */
    @PostMapping("/map")
    public ResponseEntity<ResponseDto<?>> createCollectionMap(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody CreateCollectionMapRequestDto createCollectionMapRequestDto
    ) {

        CreateCollectionMapCommand command = CreateCollectionMapCommand.builder()
                .accountId(passport.getAccountId())
                .mapTypeCode("MP000")
                .build();

        collectionUseCase.createCollectionMapUseCase(command);

        return ResponseEntity.ok(AdapterInWebMemberResponse.CREATE_COLLECTION_MAP.toResponseDto());
    }

    /**
     * 맵 컬렉션 조회
     */
    @GetMapping("/map")
    public ResponseEntity<ResponseDto<List<GetCollectionMapResponseDto>>> getCollectionMaps(
            @AuthenticationPrincipal Passport passport
    ) {

        GetCollectionMapsCommand command = GetCollectionMapsCommand.builder()
                .accountId(passport.getAccountId())
                .build();

        List<CollectionMap> collectionMaps = collectionUseCase.getCollectionMapsUseCase(command);

        List<GetCollectionMapResponseDto> getCollectionMapResponseDtos = collectionMaps.stream()
                .map(collectionMapVo -> GetCollectionMapResponseDto.builder()
                        .mapTypeCode(collectionMapVo.getMapTypeCode())
                        .mapTypeName(collectionMapVo.getMapTypeName())
                        .isIncluded(collectionMapVo.getIsIncluded())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInWebMemberResponse.GET_COLLECTION_MAPS.toResponseDto(getCollectionMapResponseDtos));
    }

    /**
     * 몽 컬렉션 조회
     * @param passport 패스 포트
     * @return 몽 컬렉션 목록
     */
    @GetMapping("/mong")
    public ResponseEntity<ResponseDto<List<GetCollectionMongResponseDto>>> getCollectionMongs(
            @AuthenticationPrincipal Passport passport
    ) {

        GetCollectionMongsCommand command = GetCollectionMongsCommand.builder()
                .accountId(passport.getAccountId())
                .build();

        List<CollectionMong> collectionMongs = collectionUseCase.getCollectionMongsUseCase(command);

        List<GetCollectionMongResponseDto> getCollectionMongResponseDtos = collectionMongs.stream()
                .map(collectionMongVo -> GetCollectionMongResponseDto.builder()
                        .mongTypeCode(collectionMongVo.getMongTypeCode())
                        .mongTypeName(collectionMongVo.getMongTypeName())
                        .isIncluded(collectionMongVo.getIsIncluded())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInWebMemberResponse.GET_COLLECTION_MONGS.toResponseDto(getCollectionMongResponseDtos));
    }
}
