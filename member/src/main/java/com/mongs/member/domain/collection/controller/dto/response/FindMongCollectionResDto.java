package com.mongs.member.domain.collection.controller.dto.response;

import com.mongs.core.entity.MongCode;
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
    public static FindMongCollectionResDto of(MongCode mongCode, Boolean isDisable) {
        return FindMongCollectionResDto.builder()
                .code(mongCode.code())
                .disable(isDisable)
                .build();
    }

    public static List<FindMongCollectionResDto> toList(List<MongCode> enableList, List<MongCode> disableList) {
        return Stream.concat(
                enableList.stream().map(mongCollection ->
                        FindMongCollectionResDto.of(mongCollection, false)),
                disableList.stream().map(mongCollection ->
                        FindMongCollectionResDto.of(mongCollection, true)))
                .sorted(Comparator.comparing(mongCode -> mongCode.code))
                .collect(Collectors.toList());
    }
}
