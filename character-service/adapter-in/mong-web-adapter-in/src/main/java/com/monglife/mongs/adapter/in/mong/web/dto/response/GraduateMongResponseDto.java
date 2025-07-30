package com.monglife.mongs.adapter.in.mong.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GraduateMongResponseDto {

    private Long mongId;

    @Builder
    public GraduateMongResponseDto(Long mongId) {
        this.mongId = mongId;
    }
}
