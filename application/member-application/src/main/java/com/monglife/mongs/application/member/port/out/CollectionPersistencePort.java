package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMapVo;
import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMongVo;
import com.monglife.mongs.domain.model.CollectionMap;
import com.monglife.mongs.domain.model.CollectionMong;

import java.util.List;
import java.util.Optional;

public interface CollectionPersistencePort {

    Optional<CollectionMap> createCollectionMapPort(CreateCollectionMapVo createCollectionMapVo);

    Optional<CollectionMong> createCollectionMongPort(CreateCollectionMongVo createCollectionMongVo);

    Boolean isExistsCollectionMapPort(Long accountId, String mapTypeCode);

    Boolean isExistsCollectionMongPort(Long accountId, String mongTypeCode);

    List<CollectionMap> getCollectionMapsPort(Long accountId);

    List<CollectionMong> getCollectionMongsPort(Long accountId);
}
