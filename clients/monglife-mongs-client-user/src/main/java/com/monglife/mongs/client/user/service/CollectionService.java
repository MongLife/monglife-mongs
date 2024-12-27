package com.monglife.mongs.client.user.service;

import com.monglife.mongs.client.user.client.CollectionClient;
import com.monglife.mongs.client.user.dto.request.CreateCollectionMapRequestDto;
import com.monglife.mongs.client.user.dto.request.CreateCollectionMongRequestDto;
import com.monglife.mongs.client.user.exception.InvalidCreateCollectionMapException;
import com.monglife.mongs.client.user.exception.InvalidCreateCollectionMongException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
