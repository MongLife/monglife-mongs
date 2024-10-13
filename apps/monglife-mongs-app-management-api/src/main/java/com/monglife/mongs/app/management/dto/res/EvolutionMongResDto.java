package com.monglife.mongs.app.management.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EvolutionMongResDto {

    private Long mongId;

    private String mongCode;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double sleep;

    private Double exp;

    private String shiftCode;

    private String stateCode;
}
