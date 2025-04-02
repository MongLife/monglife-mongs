package com.mongs.play.domain.collection.service;

import com.mongs.play.core.error.domain.CollectionErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.collection.entity.MapCollection;
import com.mongs.play.domain.collection.entity.MongCollection;
import com.mongs.play.domain.collection.repository.MapCollectionRepository;
import com.mongs.play.domain.collection.repository.MongCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final MapCollectionRepository mapCollectionRepository;
    private final MongCollectionRepository mongCollectionRepository;

    public List<MapCollection> getMapCollections(Long accountId) {
        return mapCollectionRepository.findByAccountId(accountId);
    }

    public List<MongCollection> getMongCollections(Long accountId) {
        return mongCollectionRepository.findByAccountId(accountId);
    }

    @Transactional(transactionManager = "collectionTransactionManager")
    public MapCollection addMapCollection(Long accountId, String mapCode) {
        return mapCollectionRepository.findByAccountIdAndCode(accountId, mapCode)
                .orElseGet(() -> mapCollectionRepository.save(MapCollection.builder()
                                .accountId(accountId)
                                .code(mapCode)
                                .build()));
    }

    @Transactional(transactionManager = "collectionTransactionManager")
    public MongCollection addMongCollection(Long accountId, String mongCode) {
        return mongCollectionRepository.findByAccountIdAndCode(accountId, mongCode)
                .orElseGet(() ->
                        /* 미등록 시 저장 */
                        mongCollectionRepository.save(MongCollection.builder()
                                .accountId(accountId)
                                .code(mongCode)
                                .build()));
    }

    @Transactional(transactionManager = "collectionTransactionManager")
    public void removeMapCollection(Long accountId, String mapCode) throws NotFoundException {

        mapCollectionRepository.findByAccountIdAndCode(accountId, mapCode)
                        .orElseThrow(() -> new NotFoundException(CollectionErrorCode.NOT_FOUND_MAP_COLLECTION));

        mapCollectionRepository.deleteByAccountIdAndCode(accountId, mapCode);
    }

    @Transactional(transactionManager = "collectionTransactionManager")
    public void removeMongCollection(Long accountId, String mongCode) throws NotFoundException {

        mongCollectionRepository.findByAccountIdAndCode(accountId, mongCode)
                        .orElseThrow(() -> new NotFoundException(CollectionErrorCode.NOT_FOUND_MONG_COLLECTION));

        mongCollectionRepository.deleteByAccountIdAndCode(accountId, mongCode);
    }
}
