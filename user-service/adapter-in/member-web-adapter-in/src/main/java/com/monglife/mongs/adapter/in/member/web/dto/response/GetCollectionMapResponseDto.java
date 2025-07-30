package com.monglife.mongs.adapter.in.member.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCollectionMapResponseDto {

    private String mapCode;

    private String mapName;

    private Boolean isIncluded;

    @Builder
    public GetCollectionMapResponseDto(String mapCode, String mapName, Boolean isIncluded) {
        this.mapCode = mapCode;
        this.mapName = mapName;
        this.isIncluded = isIncluded;
    }
}
