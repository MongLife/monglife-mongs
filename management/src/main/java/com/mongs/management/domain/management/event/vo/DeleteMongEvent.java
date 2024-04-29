package com.mongs.management.domain.management.event.vo;

import com.mongs.management.domain.management.service.vo.MongVo;

public record DeleteMongEvent(
        MongVo mongVo
) {
}
