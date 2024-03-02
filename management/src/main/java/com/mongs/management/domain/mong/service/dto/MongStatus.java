package com.mongs.management.domain.mong.service.dto;

import com.mongs.management.domain.mong.service.enums.MongCollapse;
import lombok.Builder;

@Builder(toBuilder = true)
public record MongStatus (
    Long mongId,
    MongCollapse collapse){
}
