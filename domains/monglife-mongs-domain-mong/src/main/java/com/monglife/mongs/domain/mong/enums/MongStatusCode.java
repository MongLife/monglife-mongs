package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongStatusCode {

    SOMNOLENCE("졸림", 0D, 0D, 0D, 0D, 10D, 0),
    HUNGRY("배고픔", 0D, 0D, 10D, 0D, 0D, 0),
    SICK("아픔", 0D, 0D, 0D, 10D, 0D, 0),
    NORMAL("정상", 0D, 0D, 0D, 0D, 0D, 0),
    ;

    private final String name;
    private final Double weightPercent;
    private final Double strengthPercent;
    private final Double satietyPercent;
    private final Double healthyPercent;
    private final Double fatiguePercent;
    private final Integer poopCount;
}