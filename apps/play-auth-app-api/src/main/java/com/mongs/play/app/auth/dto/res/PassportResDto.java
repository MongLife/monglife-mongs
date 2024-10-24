package com.mongs.play.app.auth.dto.res;

import com.mongs.play.core.vo.PassportDataVo;
import lombok.Builder;

@Builder(toBuilder = true)
public record PassportResDto(
        PassportDataVo data,
        String passportIntegrity
) {
}
