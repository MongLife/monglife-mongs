package com.mongs.core.utils;

import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

public class MongUtil {

    public static Boolean isEvolutionReady(Integer exp, MongGrade mongGrade) {
        return exp >= mongGrade.getNextGrade().getEvolutionExp() && !mongGrade.equals(MongGrade.ZERO);
    }

    public static MongState getNextState(MongState mongState, MongGrade mongGrade, Double weight, Double strength, Double satiety, Double healthy, Double sleep) {
        MongState nextState = mongState;

        for (MongState state : MongState.values()) {
            if (statusToPercent(weight, mongGrade) < state.getWeightPercent() ||
                    statusToPercent(strength, mongGrade) < state.getStrengthPercent() ||
                    statusToPercent(satiety, mongGrade) < state.getSatietyPercent() ||
                    statusToPercent(healthy, mongGrade) < state.getHealthyPercent() ||
                    statusToPercent(sleep, mongGrade) < state.getSleepPercent()
            ) {
                nextState = state;
                break;
            }
        }

        return nextState;
    }

    public static String getNextMongCode() {
        return getNextMongCode(MongGrade.EMPTY, "CH444");
    }

    public static String getNextMongCode(MongGrade mongGrade, String mongCode) {

        String nextMongCode = mongCode;

        switch (mongGrade) {
            case EMPTY -> {
                nextMongCode = "CH000";
            }
            case ZERO -> {
                nextMongCode = "CH100";
            }
            case FIRST -> {
                nextMongCode = "CH200";
            }
            case SECOND -> {
                nextMongCode = "CH300";
            }
            case THIRD, LAST -> {}
        }

        return nextMongCode;
    }

    public static MongShift getNextShiftCode(MongGrade mongGrade, MongShift mongShift) {

        MongShift nextShiftCode = mongShift;

        switch (mongGrade) {
            case ZERO, FIRST, SECOND, THIRD -> {
                nextShiftCode = MongShift.NORMAL;
            }
            case LAST -> {
                nextShiftCode = MongShift.DELETE;
            }
        }

        return nextShiftCode;
    }

    public static MongState getNextStateCode(MongGrade mongGrade, MongState mongState) {

        MongState nextStateCode = mongState;

        switch (mongGrade) {
            case ZERO, LAST -> {
                nextStateCode = MongState.NORMAL;
            }
            case FIRST, SECOND, THIRD -> {}
        }

        return nextStateCode;
    }
}
