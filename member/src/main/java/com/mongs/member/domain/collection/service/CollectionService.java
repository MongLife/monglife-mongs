package com.mongs.member.domain.collection.service;

import com.mongs.core.entity.MapCode;
import com.mongs.core.entity.MongCode;
import com.mongs.member.domain.collection.dto.response.FindMapCollectionResDto;
import com.mongs.member.domain.collection.dto.response.FindMongCollectionResDto;
import com.mongs.member.domain.collection.dto.response.RegisterMapCollectionResDto;
import com.mongs.member.domain.collection.dto.response.RegisterMongCollectionResDto;
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
    public List<FindMapCollectionResDto> findMapCollection(Long memberId) {
        List<MapCode> mapCodeList = mapCodeRepository.findAll();
        List<String> mapCollectionList = mapCollectionRepository.findByMemberId(memberId)
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
    public List<FindMongCollectionResDto> findMongCollection(Long memberId) {
        List<MongCode> mongCodeList = mongCodeRepository.findAll();
        List<String> mongCollectionList = mongCollectionRepository.findByMemberId(memberId)
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
    public RegisterMapCollectionResDto registerMapCollection(Long memberId, String mapCode) throws InvalidCodeException {
        /* mapCode 값 유효성 체크 */
        mapCodeRepository.findById(mapCode)
                .orElseThrow(() -> new InvalidCodeException(CollectionErrorCode.INVALID_MAP_CODE));

        /* 중복 등록 여부 체크 */
        MapCollection mapCollection = mapCollectionRepository.findByMemberIdAndCode(memberId, mapCode)
                .orElseGet(() ->
                        /* 미등록 시 저장 */
                        mapCollectionRepository.save(MapCollection.builder()
                        .memberId(memberId)
                        .code(mapCode)
                        .build()));

        return RegisterMapCollectionResDto.builder()
                .memberId(mapCollection.getMemberId())
                .code(mapCollection.getCode())
                .createdAt(mapCollection.getCreatedAt())
                .build();
    }

    @Transactional
    public RegisterMongCollectionResDto registerMongCollection(Long memberId, String mongCode) throws InvalidCodeException {
        /* mongCode 값 유효성 체크 */
        mongCodeRepository.findById(mongCode)
                .orElseThrow(() -> new InvalidCodeException(CollectionErrorCode.INVALID_MONG_CODE));

        /* 중복 등록 여부 체크 */
        MongCollection mongCollection = mongCollectionRepository.findByMemberIdAndCode(memberId, mongCode)
                .orElseGet(() ->
                        /* 미등록 시 저장 */
                        mongCollectionRepository.save(MongCollection.builder()
                        .memberId(memberId)
                        .code(mongCode)
                        .build()));

        return RegisterMongCollectionResDto.builder()
                .memberId(mongCollection.getMemberId())
                .code(mongCollection.getCode())
                .createdAt(mongCollection.getCreatedAt())
                .build();
    }

    @Transactional
    public void removeMapCollection(Long memberId, String mapCode) throws InvalidCodeException {
        /* mapCode 값 유효성 체크 */
        mapCodeRepository.findById(mapCode)
                .orElseThrow(() -> new InvalidCodeException(CollectionErrorCode.INVALID_MAP_CODE));

        mapCollectionRepository.deleteByMemberIdAndCode(memberId, mapCode);
    }

    @Transactional
    public void removeMongCollection(Long memberId, String mongCode) throws InvalidCodeException {
        /* mongCode 값 유효성 체크 */
        mongCodeRepository.findById(mongCode)
                .orElseThrow(() -> new InvalidCodeException(CollectionErrorCode.INVALID_MONG_CODE));

        mongCollectionRepository.deleteByMemberIdAndCode(memberId, mongCode);
    }
}
