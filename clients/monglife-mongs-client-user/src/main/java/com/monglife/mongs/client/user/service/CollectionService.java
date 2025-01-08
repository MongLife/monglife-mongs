package com.monglife.mongs.client.user.service;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.client.user.client.CollectionClient;
import com.monglife.mongs.client.user.dto.request.CreateCollectionMapRequestDto;
import com.monglife.mongs.client.user.dto.request.CreateCollectionMongRequestDto;
import com.monglife.mongs.client.user.dto.response.GetCollectionMongResponseDto;
import com.monglife.mongs.client.user.exception.InvalidCreateCollectionMapException;
import com.monglife.mongs.client.user.exception.InvalidCreateCollectionMongException;
import com.monglife.mongs.client.user.exception.InvalidGetCollectionMongException;
import com.monglife.mongs.client.user.vo.CollectionMongVo;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionClient collectionClient;

    public void createCollectionMap(String mapTypeCode) {

        try {
            CreateCollectionMapRequestDto createCollectionMapRequestDto = CreateCollectionMapRequestDto.builder()
                    .mapTypeCode(mapTypeCode)
                    .build();

            collectionClient.createCollectionMap(createCollectionMapRequestDto);

        } catch (RetryableException e) {
            throw new InvalidCreateCollectionMapException(mapTypeCode);
        }
    }

    public void createCollectionMong(String mongTypeCode) {

        try {
            CreateCollectionMongRequestDto createCollectionMongRequestDto = CreateCollectionMongRequestDto.builder()
                    .mongTypeCode(mongTypeCode)
                    .build();

            collectionClient.createCollectionMong(createCollectionMongRequestDto);

        } catch (RetryableException e) {
            throw new InvalidCreateCollectionMongException(mongTypeCode);
        }
    }

    public List<CollectionMongVo> getCollectionMongTypeCodes() {

        List<CollectionMongVo> collectionMapVos = Collections.emptyList();

        try {
            ResponseEntity<ResponseDto<List<GetCollectionMongResponseDto>>> response = collectionClient.getCollectionMongs();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                collectionMapVos = response.getBody().getResult().stream()
                        .map(getCollectionMongResponseDto -> CollectionMongVo.builder()
                                .mongTypeCode(getCollectionMongResponseDto.getMongTypeCode())
                                .mongTypeName(getCollectionMongResponseDto.getMongTypeName())
                                .isIncluded(getCollectionMongResponseDto.getIsIncluded())
                                .build())
                        .toList();
            }

        } catch (RetryableException ignored) {
            throw new InvalidGetCollectionMongException();
        }

        return collectionMapVos;
    }
}
