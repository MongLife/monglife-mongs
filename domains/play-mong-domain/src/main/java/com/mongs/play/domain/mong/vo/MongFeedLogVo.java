package com.mongs.play.domain.mong.vo;

import com.mongs.play.domain.mong.entity.MongFeedLog;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record MongFeedLogVo(
        Long mongId,
        String code,
        LocalDateTime lastBuyAt
) {
    public static MongFeedLogVo of(MongFeedLog mongFeedLog) {
        return MongFeedLogVo.builder()
                .mongId(mongFeedLog.getMongId())
                .code(mongFeedLog.getCode())
                .lastBuyAt(mongFeedLog.getUpdatedAt())
                .build();
    }

    public static List<MongFeedLogVo> toList(List<MongFeedLog> mongFeedLogList) {
        return mongFeedLogList.stream()
                .map(MongFeedLogVo::of)
                .toList();
    }
}
