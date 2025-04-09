package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.mongs.application.member.domain.CollectionMap;
import com.monglife.mongs.application.member.domain.CollectionMong;
import com.monglife.mongs.application.member.domain.Player;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.vo.CreateCollectionMapVo;
import com.monglife.mongs.application.member.port.vo.CreateCollectionMongVo;
import com.monglife.mongs.application.member.port.vo.CreateFeedbackVo;
import com.monglife.mongs.application.member.port.vo.CreatePlayerVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberPersistenceService implements MemberPersistencePort {

    @Override
    public void createCollectionMapPort(CreateCollectionMapVo createCollectionMapVo) {

    }

    @Override
    public void createCollectionMongPort(CreateCollectionMongVo createCollectionMongVo) {

    }

    @Override
    public List<CollectionMap> getCollectionMapsPort(Long accountId) {
        return List.of();
    }

    @Override
    public List<CollectionMong> getCollectionMongsPort(Long accountId) {
        return List.of();
    }

    @Override
    public void createFeedback(CreateFeedbackVo createFeedbackVo) {

    }

    @Override
    public void createPlayerPort(CreatePlayerVo createPlayerVo) {

    }

    @Override
    public Player getPlayerPort(Long accountId) {
        return null;
    }

    @Override
    public void increaseSlotCountPort(Long accountId, Integer increaseSlotCount) {

    }

    @Override
    public void increaseStarPointPort(Long accountId, Integer increaseStarPoint) {

    }

    @Override
    public void decreaseStarPointPort(Long accountId, Integer decreaseStarPoint) {

    }
}
