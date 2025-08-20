package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.port.out.vo.SearchMapVo;
import com.monglife.mongs.domain.member.model.CollectionMap;
import com.monglife.mongs.domain.member.model.CollectionMong;

import java.util.List;
import java.util.Queue;

public interface CollectionReadPort {

    Boolean isExistsCollectionMapPort(Long accountId, String mapCode);

    Boolean isExistsCollectionMongPort(Long accountId, String mongCode);

    List<CollectionMap> getCollectionMapsPort(Long accountId);

    List<CollectionMong> getCollectionMongsPort(Long accountId);

    Queue<SearchMapVo> searchMapsPort(Double latitude, Double longitude, Integer radius);
}
