package com.mongs.lifecycle.vo;

import com.mongs.lifecycle.code.MongEventCode;
import lombok.Builder;

@Builder
public record StopTaskVo(
        Long mongId,
        MongEventCode mongEventCode
) {
}
