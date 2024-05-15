package com.mongs.play.app.player.external.collection.vo;

import com.mongs.play.domain.code.entity.MapCode;
import lombok.Builder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public record FindMapCollectionVo(
        String code,
        Boolean disable
) {
    public static FindMapCollectionVo of(MapCode mapCode, Boolean isDisable) {
        return FindMapCollectionVo.builder()
                .code(mapCode.code())
                .disable(isDisable)
                .build();
    }

    public static List<FindMapCollectionVo> toList(List<MapCode> enableList, List<MapCode> disableList) {
        return Stream.concat(
                        enableList.stream().map(mapCollection ->
                                FindMapCollectionVo.of(mapCollection, false)),
                        disableList.stream().map(mapCollection ->
                                FindMapCollectionVo.of(mapCollection, true)))
                .sorted(Comparator.comparing(mapCode -> mapCode.code))
                .collect(Collectors.toList());
    }
}
