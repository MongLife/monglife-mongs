package com.mongs.core.utils;

import com.mongs.core.enums.management.MongGrade;

public class MongStatusUtil {

    /**
     * 지수 값을 퍼센트로 변환한다.
     *
     * @param status 몽의 지수 값
     * @param mongGrade 몽 레벨 단계
     * @return 지수 값을 퍼센트로 변환한 값
     */
    public static Double statusToPercent(Double status, MongGrade mongGrade) {
        double maxStatus = mongGrade.getMaxStatus();
        return status / maxStatus * 100;
    }

    /**
     * 몽 지수의 퍼센트 값을 지수 값으로 변환한다.
     * 
     * @param percent 몽의 지수 퍼센트 값
     * @param mongGrade 몽 레벨 단계
     * @return 지수 퍼센트 값을 지수 값으로 변환한 값
     */
    public static Double percentToStatus(Double percent, MongGrade mongGrade) {
        double maxStatus = mongGrade.getMaxStatus();
        return percent / 100 * maxStatus;
    }
}
