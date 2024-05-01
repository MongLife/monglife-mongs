package com.mongs.play.auth.internal.dto.res;

import com.mongs.play.app.core.vo.PassportDataVo;
import lombok.Builder;

@Builder(toBuilder = true)
public record PassportResDto(
        PassportDataVo data,
        String passportIntegrity
) {
}
