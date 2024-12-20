package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MongStateObserveResponseDto {

    private Long mongId;

    private MongStateCode stateCode;

    private Boolean isSleep;

    @Builder
    public MongStateObserveResponseDto(Long mongId, MongStateCode stateCode, Boolean isSleep) {
        this.mongId = mongId;
        this.stateCode = stateCode;
        this.isSleep = isSleep;
    }
}
