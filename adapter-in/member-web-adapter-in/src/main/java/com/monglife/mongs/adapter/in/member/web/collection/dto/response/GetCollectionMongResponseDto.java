package com.monglife.mongs.adapter.in.member.web.collection.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCollectionMongResponseDto {

    private String mongTypeCode;

    private String mongTypeName;

    private Boolean isIncluded;

    @Builder
    public GetCollectionMongResponseDto(String mongTypeCode, String mongTypeName, Boolean isIncluded) {
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.isIncluded = isIncluded;
    }
}
