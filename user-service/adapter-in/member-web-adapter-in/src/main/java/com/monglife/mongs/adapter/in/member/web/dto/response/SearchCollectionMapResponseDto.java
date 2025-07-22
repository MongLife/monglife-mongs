package com.monglife.mongs.adapter.in.member.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchCollectionMapResponseDto {

    private Boolean isFind;

    private String mapCode;

    private String mapName;

    @Builder
    public SearchCollectionMapResponseDto(Boolean isFind, String mapCode, String mapName) {
        this.isFind = isFind;
        this.mapCode = mapCode;
        this.mapName = mapName;
    }
}
