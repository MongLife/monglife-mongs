package com.mongs.play.app.player.external.collection.dto.res;

import com.mongs.play.app.player.external.collection.vo.FindMapCollectionVo;
import lombok.Builder;

import java.util.List;

@Builder
public record FindMapCollectionResDto(
        String code,
        Boolean disable
) {
    public static FindMapCollectionResDto of(FindMapCollectionVo findMapCollectionVo) {
        return FindMapCollectionResDto.builder()
                .code(findMapCollectionVo.code())
                .disable(findMapCollectionVo.disable())
                .build();
    }

    public static List<FindMapCollectionResDto> toList(List<FindMapCollectionVo> findMapCollectionVoList) {
        return findMapCollectionVoList.stream()
                .map(FindMapCollectionResDto::of)
                .toList();
    }
}