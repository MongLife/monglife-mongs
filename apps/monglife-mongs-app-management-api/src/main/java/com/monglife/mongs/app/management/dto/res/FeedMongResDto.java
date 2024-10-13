package com.monglife.mongs.app.management.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FeedMongResDto {

    private String mongId;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double sleep;

    private Double exp;

    private Integer payPoint;
}
