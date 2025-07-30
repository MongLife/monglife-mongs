package com.monglife.mongs.adapter.in.member.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchCollectionMapResponseDto {

    private Boolean isFound;

    private GetCollectionMapResponseDto data;

    @Builder
    public SearchCollectionMapResponseDto(Boolean isFound, GetCollectionMapResponseDto data) {
        this.isFound = isFound;
        this.data = data;
    }
}
