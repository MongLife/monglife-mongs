package com.mongs.collection.dto.response;

import com.mongs.core.code.Code;
import lombok.Builder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public record FindMongCollectionResDto(
        String code,
        Boolean disable
) {
    public static FindMongCollectionResDto of(Code mongCode, Boolean isDisable) {
        return FindMongCollectionResDto.builder()
                .code(mongCode.getCode())
                .disable(isDisable)
                .build();
    }

    public static List<FindMongCollectionResDto> toList(List<Code> enableList, List<Code> disableList) {
        return Stream.concat(
                enableList.stream().map(mongCollection ->
                        FindMongCollectionResDto.of(mongCollection, false)),
                disableList.stream().map(mongCollection ->
                        FindMongCollectionResDto.of(mongCollection, true)))
                .sorted(Comparator.comparing(mongCode -> mongCode.code))
                .collect(Collectors.toList());
    }
}
