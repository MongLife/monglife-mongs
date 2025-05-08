package com.monglife.mongs.adapter.in.member.web.collection.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCollectionMapResponseDto {

    private String mapTypeCode;

    private String mapTypeName;

    private Boolean isIncluded;

    @Builder
    public GetCollectionMapResponseDto(String mapTypeCode, String mapTypeName, Boolean isIncluded) {
        this.mapTypeCode = mapTypeCode;
        this.mapTypeName = mapTypeName;
        this.isIncluded = isIncluded;
    }
}
