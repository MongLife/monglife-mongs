package com.mongs.play.domain.mong.utils;

import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongState;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongStatusVo;
import com.mongs.play.domain.mong.vo.MongVo;

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

    public static MongStatusPercentVo statusToPercent(MongGrade mongGrade, MongVo mongVo) {

        double strengthPercent = MongUtil.statusToPercent(mongGrade.maxStatus, mongVo.strength());
        double satietyPercent = MongUtil.statusToPercent(mongGrade.maxStatus, mongVo.satiety());
        double healthyPercent = MongUtil.statusToPercent(mongGrade.maxStatus, mongVo.healthy());
        double sleepPercent = MongUtil.statusToPercent(mongGrade.maxStatus, mongVo.sleep());
        double expPercent = MongUtil.statusToPercent(mongGrade.evolutionExp, mongVo.exp());

        return MongStatusPercentVo.builder()
                .strength(strengthPercent)
                .satiety(satietyPercent)
                .healthy(healthyPercent)
                .sleep(sleepPercent)
                .exp(expPercent)
                .build();
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

    public static boolean isStateMatch(MongState mongState, MongStatusPercentVo mongStatusPercentVo) {
        return mongStatusPercentVo.strength() < mongState.strengthPercent ||
                mongStatusPercentVo.satiety() < mongState.satietyPercent ||
                mongStatusPercentVo.healthy() < mongState.healthyPercent ||
                mongStatusPercentVo.sleep() < mongState.sleepPercent;
    }

    public static boolean isEvolutionReady(MongGrade grade, MongShift shift, Double exp) {
        return grade.evolutionExp <= exp && !MongShift.GRADUATE_READY.equals(shift) && !MongShift.EVOLUTION_READY.equals(shift);
    }
}
