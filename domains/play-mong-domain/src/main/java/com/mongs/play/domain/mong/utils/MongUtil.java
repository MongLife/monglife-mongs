package com.mongs.play.domain.mong.utils;

import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongStatusVo;

public class MongUtil {

    public static Double statusToPercent(Double maxValue, Double status) {
        if (maxValue == 0) {
            return 0D;
        }
        return status / maxValue * 100;
    }

    public static Double percentToStatus(Double maxValue, Double percent) {
        if (maxValue == 0) {
            return 0D;
        }
        return percent / 100 * maxValue;
    }

    public static MongStatusPercentVo statusToPercent(MongGrade mongGrade, Mong mong) {

        double strengthPercent = MongUtil.statusToPercent(mongGrade.maxStatus, mong.getStrength());
        double satietyPercent = MongUtil.statusToPercent(mongGrade.maxStatus, mong.getSatiety());
        double healthyPercent = MongUtil.statusToPercent(mongGrade.maxStatus, mong.getHealthy());
        double sleepPercent = MongUtil.statusToPercent(mongGrade.maxStatus, mong.getSleep());
        double expPercent = MongUtil.statusToPercent(mongGrade.evolutionExp, mong.getExp());

        return MongStatusPercentVo.builder()
                .strength(strengthPercent)
                .satiety(satietyPercent)
                .healthy(healthyPercent)
                .sleep(sleepPercent)
                .exp(expPercent)
                .build();
    }

    public static MongStatusVo percentToStatus(MongGrade mongGrade, MongStatusPercentVo mongStatusPercentVo) {

        double strength = MongUtil.percentToStatus(mongGrade.maxStatus, mongStatusPercentVo.strength());
        double satiety = MongUtil.percentToStatus(mongGrade.maxStatus, mongStatusPercentVo.satiety());
        double healthy = MongUtil.percentToStatus(mongGrade.maxStatus, mongStatusPercentVo.healthy());
        double sleep = MongUtil.percentToStatus(mongGrade.maxStatus, mongStatusPercentVo.sleep());
        double exp = MongUtil.percentToStatus(mongGrade.evolutionExp, mongStatusPercentVo.exp());

        return MongStatusVo.builder()
                .strength(strength)
                .satiety(satiety)
                .healthy(healthy)
                .sleep(sleep)
                .exp(exp)
                .build();
    }
}
