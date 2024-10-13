package com.monglife.mongs.app.management.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.util.Strings;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class RegisterMongResDto {

    private Long mongId;

    private String name;

    private String mongCode;

    private Double weight;

    private Double healthy;

    private Double satiety;

    private Double strength;

    private Double sleep;

    private Double exp;

    private Integer poopCount;

    private Boolean isSleeping;

    private String stateCode;

    private Strings shiftCode;

    private Integer payPoint;

    private LocalDateTime born;
}
