package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongStateObserveResponseDto {

    private MongStateCode stateCode;

    private Boolean isSleep;

    @Builder
    public MongStateObserveResponseDto(MongStateCode stateCode, Boolean isSleep) {
        this.stateCode = stateCode;
        this.isSleep = isSleep;
    }
}
