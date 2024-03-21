package com.mongs.management.domain.mong.service.moduleService;

import com.mongs.management.domain.mong.client.dto.response.RegisterMapCollectionResDto;
import com.mongs.management.domain.mong.client.dto.response.RegisterMongCollectionResDto;

import java.util.Optional;

public interface CollectionService {
    Optional<RegisterMapCollectionResDto> registerMapCollection(Long accountId, String mapCode);
    Optional<RegisterMongCollectionResDto> registerMongCollection(Long accountId, String mongCode);
}