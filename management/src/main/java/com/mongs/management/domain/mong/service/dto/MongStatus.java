package com.mongs.management.domain.mong.service.dto;

import com.mongs.core.code.enums.management.MongCollapse;
import lombok.Builder;

@Builder(toBuilder = true)
public record MongStatus (
    Long mongId,
    MongCollapse collapse){
}
