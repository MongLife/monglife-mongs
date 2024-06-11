package com.mongs.play.app.player.internal.collection.service;

import com.mongs.play.app.player.internal.collection.vo.*;
import com.mongs.play.domain.code.entity.MapCode;
import com.mongs.play.domain.code.entity.MongCode;
import com.mongs.play.domain.code.service.CodeService;
import com.mongs.play.domain.collection.entity.MapCollection;
import com.mongs.play.domain.collection.entity.MongCollection;
import com.mongs.play.domain.collection.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerInternalCollectionService {

    private final CodeService codeService;
    private final CollectionService collectionService;

    @Transactional
    public RegisterMapCollectionVo registerMapCollection(Long accountId, String code) {

        /* mapCode 값 유효성 체크 */
        MapCode mapCode = codeService.getMapCode(code);

        MapCollection mapCollection = collectionService.addMapCollection(accountId, mapCode.getCode());

        return RegisterMapCollectionVo.builder()
                .accountId(mapCollection.getAccountId())
                .code(mapCollection.getCode())
                .createdAt(mapCollection.getCreatedAt())
                .build();
    }

    @Transactional
    public RegisterMongCollectionVo registerMongCollection(Long accountId, String code) {

        /* mongCode 값 유효성 체크 */
        MongCode mongCode = codeService.getMongCode(code);

        MongCollection mongCollection = collectionService.addMongCollection(accountId, mongCode.getCode());

        return RegisterMongCollectionVo.builder()
                .accountId(mongCollection.getAccountId())
                .code(mongCollection.getCode())
                .createdAt(mongCollection.getCreatedAt())
                .build();
    }

    @Transactional
    public RemoveMapCollectionVo removeMapCollection(Long accountId, String code) {

        /* mapCode 값 유효성 체크 */
        MapCode mapCode = codeService.getMapCode(code);

        collectionService.removeMapCollection(accountId, mapCode.getCode());

        return RemoveMapCollectionVo.builder()
                .accountId(accountId)
                .code(mapCode.getCode())
                .build();
    }

    @Transactional
    public RemoveMongCollectionVo removeMongCollection(Long accountId, String code) {

        /* mongCode 값 유효성 체크 */
        MongCode mongCode = codeService.getMongCode(code);

        collectionService.removeMongCollection(accountId, mongCode.getCode());

        return RemoveMongCollectionVo.builder()
                .accountId(accountId)
                .code(mongCode.getCode())
                .build();
    }
}
