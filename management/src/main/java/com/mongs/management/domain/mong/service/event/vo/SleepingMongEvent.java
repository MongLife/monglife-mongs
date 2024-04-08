package com.mongs.management.domain.mong.service.event.vo;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;

public record SleepingMongEvent(
        MongVo mongVo
) {
}
