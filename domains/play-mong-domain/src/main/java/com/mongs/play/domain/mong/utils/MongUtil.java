package com.mongs.play.domain.mong.utils;

import com.mongs.play.domain.mong.enums.MongGrade;

public class MongUtil {
    public static Double statusToPercent(MongGrade mongGrade, Double status) {
        return status / mongGrade.maxStatus * 100;
    }

    public static Double percentToStatus(MongGrade mongGrade, Double percent) {
        return percent / 100 * mongGrade.maxStatus;
    }
}
