package com.monglife.mongs.app.management.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class FindMongResDto {

    private Long mongId;

    private String name;

    private String mongCode;

    private Double weight;

    private Double healthy;

    private Double satiety;

    private Double strength;

    private Double sleep;

    private Integer poopCount;

    private Boolean isSleeping;

    private Double exp;

    private String stateCode;

    private String shiftCode;

    private Integer payPoint;

    private LocalDateTime born;
}
