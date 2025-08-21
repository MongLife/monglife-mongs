package com.monglife.mongs.application.member.port.in.service;

import com.monglife.mongs.application.member.port.exception.InvalidCreateCollectionMapException;
import com.monglife.mongs.application.member.port.exception.InvalidCreateCollectionMongException;
import com.monglife.mongs.application.member.port.in.CollectionUseCase;
import com.monglife.mongs.application.member.port.in.command.*;
import com.monglife.mongs.application.member.port.out.CollectionPersistencePort;
import com.monglife.mongs.application.member.port.out.CollectionReadPort;
import com.monglife.mongs.application.member.port.out.MapStoreReadPort;
import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMapVo;
import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMongVo;
import com.monglife.mongs.application.member.port.out.vo.SearchMapVo;
import com.monglife.mongs.domain.member.model.CollectionMap;
import com.monglife.mongs.domain.member.model.CollectionMong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class CollectionService implements CollectionUseCase {

    private final CollectionPersistencePort collectionPersistencePort;

    private final CollectionReadPort collectionReadPort;

    private final MapStoreReadPort mapStoreReadPort;

    /**
     * 맵 컬렉션 등록
     */
    @Override
    @Transactional
    public void createCollectionMapUseCase(CreateCollectionMapCommand command) {

        if (!collectionReadPort.isExistsCollectionMapPort(command.getAccountId(), command.getMapCode())) {
            collectionPersistencePort.createCollectionMapPort(CreateCollectionMapVo.builder()
                    .accountId(command.getAccountId())
                    .mapCode(command.getMapCode())
                    .build())
                    .orElseThrow(InvalidCreateCollectionMapException::new);
        }
    }

    /**
     * 몽 컬렉션 등록
     */
    @Override
    @Transactional
    public void createCollectionMongUseCase(CreateCollectionMongCommand command) {

        if (!collectionReadPort.isExistsCollectionMongPort(command.getAccountId(), command.getMongCode())) {
            collectionPersistencePort.createCollectionMongPort(CreateCollectionMongVo.builder()
                    .accountId(command.getAccountId())
                    .mongCode(command.getMongCode())
                    .build())
                    .orElseThrow(InvalidCreateCollectionMongException::new);
        }
    }

    /**
     * 맵 컬렉션 목록 조회
     */
    @Override
    @Transactional
    public List<CollectionMap> getCollectionMapsUseCase(GetCollectionMapsCommand command) {
        return collectionReadPort.getCollectionMapsPort(command.getAccountId());
    }

    /**
     * 몽 컬렉션 목록 조회
     */
    @Override
    @Transactional
    public List<CollectionMong> getCollectionMongsUseCase(GetCollectionMongsCommand command) {
        return collectionReadPort.getCollectionMongsPort(command.getAccountId());
    }

    /**
     * 맵 컬렉션 탐색
     */
    @Override
    public CollectionMap searchCollectionMapUseCase(SearchCollectionMapCommand command) {

        Queue<SearchMapVo> searchMapVoQueue = mapStoreReadPort.searchMapsPort(command.getLatitude(), command.getLongitude(), 15);

        while (!searchMapVoQueue.isEmpty()) {

            SearchMapVo searchMapVo = searchMapVoQueue.poll();

            if (!collectionReadPort.isExistsCollectionMapPort(command.getAccountId(), searchMapVo.getMapCode())) {
                return collectionPersistencePort.createCollectionMapPort(CreateCollectionMapVo.builder()
                                .accountId(command.getAccountId())
                                .mapCode(searchMapVo.getMapCode())
                                .build())
                        .orElseThrow(InvalidCreateCollectionMapException::new);
            }
        }

        return null;
    }
}
