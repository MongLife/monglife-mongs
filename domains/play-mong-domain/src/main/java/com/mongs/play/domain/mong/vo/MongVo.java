package com.mongs.play.domain.mong.vo;

import com.mongs.play.domain.mong.entity.Mong;
import lombok.Builder;

@Builder(toBuilder = true)
public record MongVo(
        Mong pastMong,
        Mong mong
) {
}