package com.mongs.play.app.player.external.collection.vo;

import com.mongs.play.domain.code.entity.MongCode;
import lombok.Builder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public record FindMongCollectionVo(
        String code,
        Boolean disable
) {
    public static FindMongCollectionVo of(MongCode mongCode, Boolean isDisable) {
        return FindMongCollectionVo.builder()
                .code(mongCode.code())
                .disable(isDisable)
                .build();
    }

    public static List<FindMongCollectionVo> toList(List<MongCode> enableList, List<MongCode> disableList) {
        return Stream.concat(
                enableList.stream().map(mongCollection ->
                        FindMongCollectionVo.of(mongCollection, false)),
                disableList.stream().map(mongCollection ->
                        FindMongCollectionVo.of(mongCollection, true)))
                .sorted(Comparator.comparing(mongCode -> mongCode.code))
                .collect(Collectors.toList());
    }
}
