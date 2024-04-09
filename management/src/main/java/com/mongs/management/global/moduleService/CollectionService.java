package com.mongs.management.global.moduleService;

import com.mongs.management.domain.management.client.dto.response.RegisterMapCollectionResDto;
import com.mongs.management.domain.management.client.dto.response.RegisterMongCollectionResDto;

import java.util.Optional;

public interface CollectionService {
    Optional<RegisterMapCollectionResDto> registerMapCollection(String mapCode);
    Optional<RegisterMongCollectionResDto> registerMongCollection(String mongCode);
}
