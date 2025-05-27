package com.monglife.mongs.adapter.in.member.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCollectionMongResponseDto {

    private String mongCode;

    private String mongName;

    private Boolean isIncluded;

    @Builder
    public GetCollectionMongResponseDto(String mongCode, String mongName, Boolean isIncluded) {
        this.mongCode = mongCode;
        this.mongName = mongName;
        this.isIncluded = isIncluded;
    }
}
