package com.mongs.member.domain.collection.service;

import com.mongs.core.entity.MapCode;
import com.mongs.core.entity.MongCode;
import com.mongs.member.domain.collection.controller.dto.response.*;
import com.mongs.member.domain.collection.entity.MapCollection;
import com.mongs.member.domain.collection.entity.MongCollection;
import com.mongs.member.domain.collection.exception.CollectionErrorCode;
import com.mongs.member.domain.collection.exception.InvalidCodeException;
import com.mongs.member.domain.collection.repository.MapCodeRepository;
import com.mongs.member.domain.collection.repository.MapCollectionRepository;
import com.mongs.member.domain.collection.repository.MongCodeRepository;
import com.mongs.member.domain.collection.repository.MongCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final MapCollectionRepository mapCollectionRepository;
    private final MongCollectionRepository mongCollectionRepository;
    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;

    @Transactional(readOnly = true)
    public List<FindMapCollectionResDto> findMapCollection(Long accountId) {
        List<MapCode> mapCodeList = mapCodeRepository.findAll();
        List<String> mapCollectionList = mapCollectionRepository.findByAccountId(accountId)
                    .stream()
                    .map(MapCollection::getCode)
                    .toList();

        List<MapCode> enableList = mapCodeList.stream()
                .filter(mapCode -> mapCollectionList.contains(mapCode.code()))
                .toList();

        List<MapCode> disableList = mapCodeList.stream()
                .filter(mapCode -> !mapCollectionList.contains(mapCode.code()))
                .toList();

        return FindMapCollectionResDto.toList(enableList, disableList);
    }

    @Transactional(readOnly = true)
    public List<FindMongCollectionResDto> findMongCollection(Long accountId) {
        List<MongCode> mongCodeList = mongCodeRepository.findAll();
        List<String> mongCollectionList = mongCollectionRepository.findByAccountId(accountId)
                .stream()
                .map(MongCollection::getCode)
                .toList();

        List<MongCode> enableList = mongCodeList.stream()
                .filter(mongCode -> mongCollectionList.contains(mongCode.code()))
                .toList();

        List<MongCode> disableList = mongCodeList.stream()
                .filter(mongCode -> !mongCollectionList.contains(mongCode.code()))
                .toList();

        return FindMongCollectionResDto.toList(enableList, disableList);
    }

    @Transactional
    public RegisterMapCollectionResDto registerMapCollection(Long accountId, String mapCode) throws InvalidCodeException {
        /* mapCode 값 유효성 체크 */
        mapCodeRepository.findById(mapCode)
                .orElseThrow(() -> new InvalidCodeException(CollectionErrorCode.INVALID_MAP_CODE));

        /* 중복 등록 여부 체크 */
        MapCollection mapCollection = mapCollectionRepository.findByAccountIdAndCode(accountId, mapCode)
                .orElseGet(() ->
                        /* 미등록 시 저장 */
                        mapCollectionRepository.save(MapCollection.builder()
                        .accountId(accountId)
                        .code(mapCode)
                        .build()));

        return RegisterMapCollectionResDto.builder()
                .accountId(mapCollection.getAccountId())
                .code(mapCollection.getCode())
                .createdAt(mapCollection.getCreatedAt())
                .build();
    }

    @Transactional
    public RegisterMongCollectionResDto registerMongCollection(Long accountId, String mongCode) throws InvalidCodeException {
        /* mongCode 값 유효성 체크 */
        mongCodeRepository.findById(mongCode)
                .orElseThrow(() -> new InvalidCodeException(CollectionErrorCode.INVALID_MONG_CODE));

        /* 중복 등록 여부 체크 */
        MongCollection mongCollection = mongCollectionRepository.findByAccountIdAndCode(accountId, mongCode)
                .orElseGet(() ->
                        /* 미등록 시 저장 */
                        mongCollectionRepository.save(MongCollection.builder()
                        .accountId(accountId)
                        .code(mongCode)
                        .build()));

        return RegisterMongCollectionResDto.builder()
                .accountId(mongCollection.getAccountId())
                .code(mongCollection.getCode())
                .createdAt(mongCollection.getCreatedAt())
                .build();
    }

    @Transactional
    public RemoveMapCollectionResDto removeMapCollection(Long accountId, String mapCode) throws InvalidCodeException {
        /* mapCode 값 유효성 체크 */
        mapCodeRepository.findById(mapCode)
                .orElseThrow(() -> new InvalidCodeException(CollectionErrorCode.INVALID_MAP_CODE));

        mapCollectionRepository.deleteByAccountIdAndCode(accountId, mapCode);

        return RemoveMapCollectionResDto.builder()
                .accountId(accountId)
                .build();
    }

    @Transactional
    public RemoveMongCollectionResDto removeMongCollection(Long accountId, String mongCode) throws InvalidCodeException {
        /* mongCode 값 유효성 체크 */
        mongCodeRepository.findById(mongCode)
                .orElseThrow(() -> new InvalidCodeException(CollectionErrorCode.INVALID_MONG_CODE));

        mongCollectionRepository.deleteByAccountIdAndCode(accountId, mongCode);

        return RemoveMongCollectionResDto.builder()
                .accountId(accountId)
                .build();
    }
}
