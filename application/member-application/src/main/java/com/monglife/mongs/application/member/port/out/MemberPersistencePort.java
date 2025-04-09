package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.domain.CollectionMap;
import com.monglife.mongs.application.member.domain.CollectionMong;
import com.monglife.mongs.application.member.domain.Player;
import com.monglife.mongs.application.member.port.vo.CreateCollectionMapVo;
import com.monglife.mongs.application.member.port.vo.CreateCollectionMongVo;
import com.monglife.mongs.application.member.port.vo.CreateFeedbackVo;
import com.monglife.mongs.application.member.port.vo.CreatePlayerVo;

import java.util.List;

public interface MemberPersistencePort {

    void createCollectionMapPort(CreateCollectionMapVo createCollectionMapVo);

    void createCollectionMongPort(CreateCollectionMongVo createCollectionMongVo);

    List<CollectionMap> getCollectionMapsPort(Long accountId);

    List<CollectionMong> getCollectionMongsPort(Long accountId);

    void createFeedback(CreateFeedbackVo createFeedbackVo);

    void createPlayerPort(CreatePlayerVo createPlayerVo);

    Player getPlayerPort(Long accountId);

    void increaseSlotCountPort(Long accountId, Integer increaseSlotCount);

    void increaseStarPointPort(Long accountId, Integer increaseStarPoint);

    void decreaseStarPointPort(Long accountId, Integer decreaseStarPoint);
}
