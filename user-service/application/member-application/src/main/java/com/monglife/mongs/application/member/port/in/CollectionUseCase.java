package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.*;
import com.monglife.mongs.domain.member.model.CollectionMap;
import com.monglife.mongs.domain.member.model.CollectionMong;

import java.util.List;

public interface CollectionUseCase {

    /**
     * 맵 컬렉션 등록
     */
    void createCollectionMapUseCase(CreateCollectionMapCommand command);

    /**
     * 몽 컬렉션 등록
     */
    void createCollectionMongUseCase(CreateCollectionMongCommand command);

    /**
     * 맵 컬렉션 목록 조회
     */
    List<CollectionMap> getCollectionMapsUseCase(GetCollectionMapsCommand command);
    
    /**
     * 몽 컬렉션 목록 조회
     */
    List<CollectionMong> getCollectionMongsUseCase(GetCollectionMongsCommand command);

    CollectionMap searchCollectionMapUseCase(SearchCollectionMapCommand command);
}
