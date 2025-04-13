package com.monglife.mongs.application.member.port.in.service;

import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMapVo;
import com.monglife.mongs.application.member.port.out.vo.CreateCollectionMongVo;
import com.monglife.mongs.domain.model.CollectionMap;
import com.monglife.mongs.domain.model.CollectionMong;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMapCommand;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMongCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMapsCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMongsCommand;
import com.monglife.mongs.application.member.port.in.CollectionUseCase;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService implements CollectionUseCase {

    private final MemberPersistencePort memberPersistencePort;

    /**
     * 맵 컬렉션 등록
     */
    @Override
    @Transactional
    public void createCollectionMapUseCase(CreateCollectionMapCommand command) {

        if (!memberPersistencePort.isExistsCollectionMap(command.getAccountId(), command.getMapTypeCode())) {
            memberPersistencePort.createCollectionMapPort(CreateCollectionMapVo.builder()
                    .accountId(command.getAccountId())
                    .mapTypeCode(command.getMapTypeCode())
                    .build());
        }
    }

    /**
     * 몽 컬렉션 등록
     */
    @Override
    @Transactional
    public void createCollectionMongUseCase(CreateCollectionMongCommand command) {

        if (!memberPersistencePort.isExistsCollectionMong(command.getAccountId(), command.getMongTypeCode())) {
            memberPersistencePort.createCollectionMongPort(CreateCollectionMongVo.builder()
                    .accountId(command.getAccountId())
                    .mongTypeCode(command.getMongTypeCode())
                    .build());
        }
    }

    /**
     * 맵 컬렉션 목록 조회
     */
    @Override
    @Transactional
    public List<CollectionMap> getCollectionMapsUseCase(GetCollectionMapsCommand command) {

        return memberPersistencePort.getCollectionMapsPort(command.getAccountId());
    }

    /**
     * 몽 컬렉션 목록 조회
     */
    @Override
    @Transactional
    public List<CollectionMong> getCollectionMongsUseCase(GetCollectionMongsCommand command) {

        return memberPersistencePort.getCollectionMongsPort(command.getAccountId());
    }
}
