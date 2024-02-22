package com.mongs.collection.service;

import com.mongs.collection.dto.response.FindMapCollectionResDto;
import com.mongs.collection.dto.response.FindMongCollectionResDto;
import com.mongs.collection.entity.MapCollection;
import com.mongs.collection.entity.MongCollection;
import com.mongs.collection.repository.MapCollectionRepository;
import com.mongs.collection.repository.MongCollectionRepository;
import com.mongs.core.code.Code;
import com.mongs.core.code.CodeRepository;
import com.mongs.core.code.GroupCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CodeRepository codeRepository;
    private final MapCollectionRepository mapCollectionRepository;
    private final MongCollectionRepository mongCollectionRepository;

    public List<FindMapCollectionResDto> findMapCollection(Long memberId) {
        List<Code> mapCodeList = codeRepository.findByGroupCode(GroupCode.MAP.getGroupCode());
        List<String> mapCollectionList = mapCollectionRepository.findByMemberId(memberId)
                    .stream()
                    .map(MapCollection::getCode)
                    .toList();

        List<Code> enableList = mapCodeList.stream()
                .filter(code -> mapCollectionList.contains(code.getCode()))
                .toList();

        List<Code> disableList = mapCodeList.stream()
                .filter(code -> !mapCollectionList.contains(code.getCode()))
                .toList();

        return FindMapCollectionResDto.toList(enableList, disableList);
    }

    public List<FindMongCollectionResDto> findMongCollection(Long memberId) {
        List<Code> mongCodeList = codeRepository.findByGroupCode(GroupCode.MONG.getGroupCode());
        List<String> mongCollectionList = mongCollectionRepository.findByMemberId(memberId)
                .stream()
                .map(MongCollection::getCode)
                .toList();

        List<Code> enableList = mongCodeList.stream()
                .filter(code -> mongCollectionList.contains(code.getCode()))
                .toList();

        List<Code> disableList = mongCodeList.stream()
                .filter(code -> !mongCollectionList.contains(code.getCode()))
                .toList();

        return FindMongCollectionResDto.toList(enableList, disableList);
    }
}
