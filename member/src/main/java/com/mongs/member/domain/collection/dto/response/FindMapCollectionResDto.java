package com.mongs.member.domain.collection.dto.response;

import com.mongs.core.entity.MapCode;
import lombok.Builder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public record FindMapCollectionResDto(
        String code,
        Boolean disable
) {
    public static FindMapCollectionResDto of(MapCode mapCode, Boolean isDisable) {
        return FindMapCollectionResDto.builder()
                .code(mapCode.code())
                .disable(isDisable)
                .build();
    }

    public static List<FindMapCollectionResDto> toList(List<MapCode> enableList, List<MapCode> disableList) {
        return Stream.concat(
                enableList.stream().map(mapCollection ->
                        FindMapCollectionResDto.of(mapCollection, false)),
                disableList.stream().map(mapCollection ->
                        FindMapCollectionResDto.of(mapCollection, true)))
                .sorted(Comparator.comparing(mapCode -> mapCode.code))
                .collect(Collectors.toList());
    }
}
