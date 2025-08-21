package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMapEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMongEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.CollectionMapRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.CollectionMongRepository;
import com.monglife.mongs.application.member.port.out.CollectionReadPort;
import com.monglife.mongs.domain.member.model.CollectionMap;
import com.monglife.mongs.domain.member.model.CollectionMong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionReadService implements CollectionReadPort {

    private final CollectionMapRepository collectionMapRepository;

    private final CollectionMongRepository collectionMongRepository;

    /**
     * 컬렉션 맵 존재 여부 조회
     * @param accountId 회원 ID
     * @param mapCode 맵 타입 코드
     * @return 컬렉션 맵 존재 여부
     */
    @Override
    @Transactional
    public Boolean isExistsCollectionMapPort(Long accountId, String mapCode) {
        return collectionMapRepository.existsByAccountIdAndComnCode(accountId, mapCode);
    }

    /**
     * 컬렉션 몽 존재 여부 조회
     * @param accountId 회원 ID
     * @param mongCode 몽 타입 코드
     * @return 컬렉션 몽 존재 여부
     */
    @Override
    @Transactional
    public Boolean isExistsCollectionMongPort(Long accountId, String mongCode) {
        return collectionMongRepository.existsByAccountIdAndComnCode(accountId, mongCode);
    }

    /**
     * 컬렉션 맵 목록 조회
     * @param accountId 회원 ID
     * @return 컬렉션 맵 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<CollectionMap> getCollectionMapsPort(Long accountId) {

        List<CollectionMapEntity> collectionMapEntities = collectionMapRepository.findByAccountId(accountId);

        return collectionMapEntities.stream()
                .sorted(Comparator.comparing(o -> o.getComn().getCode()))
                .map(CollectionMapEntity::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 컬렉션 몽 목록 조회
     * @param accountId 회원 ID
     * @return 컬렉션 몽 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<CollectionMong> getCollectionMongsPort(Long accountId) {

        List<CollectionMongEntity> collectionMongEntities = collectionMongRepository.findByAccountId(accountId);

        return collectionMongEntities.stream()
                .sorted(Comparator.comparing(o -> o.getComn().getCode()))
                .map(CollectionMongEntity::toDomain)
                .collect(Collectors.toList());
    }
}
