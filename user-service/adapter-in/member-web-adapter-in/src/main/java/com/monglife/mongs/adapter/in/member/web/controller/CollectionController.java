package com.monglife.mongs.adapter.in.member.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.member.web.dto.response.GetCollectionMapResponseDto;
import com.monglife.mongs.adapter.in.member.web.dto.response.GetCollectionMongResponseDto;
import com.monglife.mongs.adapter.in.member.web.enums.AdapterInMemberWebResponse;
import com.monglife.mongs.application.member.port.in.CollectionUseCase;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMapsCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMongsCommand;
import com.monglife.mongs.domain.member.model.CollectionMap;
import com.monglife.mongs.domain.member.model.CollectionMong;
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

    private final CollectionUseCase collectionUseCase;

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
                        .mapCode(collectionMapVo.getMapCode())
                        .mapName(collectionMapVo.getMapName())
                        .isIncluded(collectionMapVo.getIsIncluded())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInMemberWebResponse.GET_COLLECTION_MAPS.toResponseDto(getCollectionMapResponseDtos));
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
                        .mongCode(collectionMongVo.getMongCode())
                        .mongName(collectionMongVo.getMongName())
                        .isIncluded(collectionMongVo.getIsIncluded())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInMemberWebResponse.GET_COLLECTION_MONGS.toResponseDto(getCollectionMongResponseDtos));
    }
}
