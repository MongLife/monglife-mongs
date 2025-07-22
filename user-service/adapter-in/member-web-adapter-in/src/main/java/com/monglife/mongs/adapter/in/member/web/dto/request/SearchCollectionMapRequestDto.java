package com.monglife.mongs.adapter.in.member.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchCollectionMapRequestDto {

    private Double latitude;

    private Double longitude;

    @Builder
    public SearchCollectionMapRequestDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
