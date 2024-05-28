package com.mongs.play.app.player.external.collection.service;

import com.mongs.play.app.player.external.collection.vo.FindMapCollectionVo;
import com.mongs.play.app.player.external.collection.vo.FindMongCollectionVo;
import com.mongs.play.domain.code.entity.MapCode;
import com.mongs.play.domain.code.entity.MongCode;
import com.mongs.play.domain.code.service.CodeService;
import com.mongs.play.domain.collection.entity.MapCollection;
import com.mongs.play.domain.collection.entity.MongCollection;
import com.mongs.play.domain.collection.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerExternalCollectionService {

    private final CodeService codeService;
    private final CollectionService collectionService;

    @Transactional(value = "chainedTransactionManager", readOnly = true)
    public List<FindMapCollectionVo> findMapCollection(Long accountId) {
        List<MapCode> mapCodeList = codeService.getMapCode();
        List<String> mapCollectionList = collectionService.getMapCollections(accountId)
                .stream()
                .map(MapCollection::getCode)
                .toList();

        List<MapCode> enableList = mapCodeList.stream()
                .filter(mapCode -> mapCollectionList.contains(mapCode.getCode()))
                .toList();

        List<MapCode> disableList = mapCodeList.stream()
                .filter(mapCode -> !mapCollectionList.contains(mapCode.getCode()))
                .toList();

        return FindMapCollectionVo.toList(enableList, disableList);
    }

    @Transactional(value = "chainedTransactionManager", readOnly = true)
    public List<FindMongCollectionVo> findMongCollection(Long accountId) {
        List<MongCode> mongCodeList = codeService.getMongCode();
        List<String> mongCollectionList = collectionService.getMongCollections(accountId)
                .stream()
                .map(MongCollection::getCode)
                .toList();

        List<MongCode> enableList = mongCodeList.stream()
                .filter(mongCode -> mongCollectionList.contains(mongCode.getCode()))
                .toList();

        List<MongCode> disableList = mongCodeList.stream()
                .filter(mongCode -> !mongCollectionList.contains(mongCode.getCode()))
                .toList();

        return FindMongCollectionVo.toList(enableList, disableList);
    }
}
