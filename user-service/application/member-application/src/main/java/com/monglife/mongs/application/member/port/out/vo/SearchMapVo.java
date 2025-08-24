package com.monglife.mongs.application.member.port.out.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchMapVo {

    private final String mapCode;

    private final Double longitude;

    private final Double latitude;

    private final Double distance;

    @Builder
    public SearchMapVo(String mapCode, Double longitude, Double latitude, Double distance) {
        this.mapCode = mapCode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
    }
}
