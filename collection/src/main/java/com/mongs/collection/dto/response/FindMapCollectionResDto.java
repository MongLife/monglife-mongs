package com.mongs.collection.dto.response;

import com.mongs.core.code.Code;
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
    public static FindMapCollectionResDto of(Code mapCode, Boolean isDisable) {
        return FindMapCollectionResDto.builder()
                .code(mapCode.getCode())
                .disable(isDisable)
                .build();
    }

    public static List<FindMapCollectionResDto> toList(List<Code> enableList, List<Code> disableList) {
        return Stream.concat(
                enableList.stream().map(mapCollection ->
                        FindMapCollectionResDto.of(mapCollection, false)),
                disableList.stream().map(mapCollection ->
                        FindMapCollectionResDto.of(mapCollection, true)))
                .sorted(Comparator.comparing(mapCode -> mapCode.code))
                .collect(Collectors.toList());
    }
}
