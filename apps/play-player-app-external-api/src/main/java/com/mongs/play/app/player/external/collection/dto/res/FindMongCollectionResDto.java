package com.mongs.play.app.player.external.collection.dto.res;

import com.mongs.play.app.player.external.collection.vo.FindMongCollectionVo;
import lombok.Builder;

import java.util.List;

@Builder
public record FindMongCollectionResDto(
        String code,
        Boolean disable
) {
    public static FindMongCollectionResDto of(FindMongCollectionVo findMongCollectionVo) {
        return FindMongCollectionResDto.builder()
                .code(findMongCollectionVo.code())
                .disable(findMongCollectionVo.disable())
                .build();
    }

    public static List<FindMongCollectionResDto> toList(List<FindMongCollectionVo> findMongCollectionVoList) {
        return findMongCollectionVoList.stream()
                .map(FindMongCollectionResDto::of)
                .toList();
    }
}
