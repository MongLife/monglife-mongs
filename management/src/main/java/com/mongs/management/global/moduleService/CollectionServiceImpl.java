package com.mongs.management.global.moduleService;

import com.mongs.management.domain.management.client.CollectionClient;
import com.mongs.management.domain.management.client.dto.request.RegisterMapCollectionReqDto;
import com.mongs.management.domain.management.client.dto.request.RegisterMongCollectionReqDto;
import com.mongs.management.domain.management.client.dto.response.RegisterMapCollectionResDto;
import com.mongs.management.domain.management.client.dto.response.RegisterMongCollectionResDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final CollectionClient collectionClient;

    public Optional<RegisterMapCollectionResDto> registerMapCollection(String mapCode) {
        try {
            ResponseEntity<RegisterMapCollectionResDto> response =
                    collectionClient.registerMapCollection(RegisterMapCollectionReqDto.builder()
                            .mapCode(mapCode)
                            .build());

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("registerMapCollection 통신 실패 : {}", e.getMessage());
        }

        return Optional.empty();
    }


    public Optional<RegisterMongCollectionResDto> registerMongCollection(String mongCode) {
        try {
            ResponseEntity<RegisterMongCollectionResDto> response =
                    collectionClient.registerMongCollection(RegisterMongCollectionReqDto.builder()
                            .mongCode(mongCode)
                            .build());

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("registerMongCollection 통신 실패 : {}", e.getMessage());
        }

        return Optional.empty();
    }
}
