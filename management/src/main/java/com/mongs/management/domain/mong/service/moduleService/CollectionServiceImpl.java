package com.mongs.management.domain.mong.service.moduleService;

import com.mongs.management.domain.mong.client.CollectionClient;
import com.mongs.management.domain.mong.client.dto.request.RegisterMapCollectionReqDto;
import com.mongs.management.domain.mong.client.dto.request.RegisterMongCollectionReqDto;
import com.mongs.management.domain.mong.client.dto.response.RegisterMapCollectionResDto;
import com.mongs.management.domain.mong.client.dto.response.RegisterMongCollectionResDto;
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

    public Optional<RegisterMapCollectionResDto> registerMapCollection(Long accountId, String mapCode) {
        try {
            ResponseEntity<RegisterMapCollectionResDto> response =
                    collectionClient.registerMapCollection(accountId, RegisterMapCollectionReqDto.builder()
                            .mapCode(mapCode)
                            .build());

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("[{}] registerMapCollection 통신 실패 : {}", accountId, e.getMessage());
        }

        return Optional.empty();
    }


    public Optional<RegisterMongCollectionResDto> registerMongCollection(Long accountId, String mongCode) {
        try {
            ResponseEntity<RegisterMongCollectionResDto> response =
                    collectionClient.registerMongCollection(accountId, RegisterMongCollectionReqDto.builder()
                            .mongCode(mongCode)
                            .build());

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("[{}] registerMongCollection 통신 실패 : {}", accountId, e.getMessage());
        }

        return Optional.empty();
    }
}
