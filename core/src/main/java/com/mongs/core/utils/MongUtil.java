package com.mongs.core.utils;

import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

public class MongUtil {


    public static boolean isFirstGrade(MongGrade mongGrade) {
        return mongGrade.equals(MongGrade.FIRST);
    }

    public static boolean isLastGrade(MongGrade mongGrade) {
        return mongGrade.equals(MongGrade.LAST);
    }

    public static Boolean isEvolutionReady(Integer exp, MongGrade mongGrade) {
        boolean isGradeZero = mongGrade.equals(MongGrade.ZERO);
        boolean isFullExp = exp >= mongGrade.getNextGrade().getEvolutionExp();

        return isFullExp && !isGradeZero;
    }

    public static MongState getNextState(MongState mongState, MongGrade mongGrade, Double weight, Double strength, Double satiety, Double healthy, Double sleep) {
        MongState nextState;
        double weightPercent = statusToPercent(weight, mongGrade);
        double strengthPercent = statusToPercent(strength, mongGrade);
        double satietyPercent = statusToPercent(satiety, mongGrade);
        double healthyPercent = statusToPercent(healthy, mongGrade);
        double sleepPercent = statusToPercent(sleep, mongGrade);

        if (isStateMatch(MongState.SOMNOLENCE, weightPercent, strengthPercent, satietyPercent, healthyPercent, sleepPercent)) {
            nextState = MongState.SOMNOLENCE;
        } else if (isStateMatch(MongState.HUNGRY, weightPercent, strengthPercent, satietyPercent, healthyPercent, sleepPercent)) {
            nextState = MongState.HUNGRY;
        } else if (isStateMatch(MongState.SICK, weightPercent, strengthPercent, satietyPercent, healthyPercent, sleepPercent)) {
            nextState = MongState.SICK;
        } else {
            nextState = MongState.NORMAL;
        }
//
//        for (MongState state : MongState.values()) {
//            System.out.println(state + "/" + state.getWeightPercent() + ", " + state.getStrengthPercent() + ", " + state.getSatietyPercent() + ", " + state.getHealthyPercent() + ", " + state.getSleepPercent());
//            if (weightPercent < state.getWeightPercent() ||
//                strengthPercent < state.getStrengthPercent() ||
//                satietyPercent < state.getSatietyPercent() ||
//                healthyPercent < state.getHealthyPercent() ||
//                sleepPercent < state.getSleepPercent()
//            ) {
//                nextState = state;
//                break;
//            }
//        }
//        System.out.println(nextState + "/" + weightPercent + ", " + strengthPercent + ", " + satietyPercent + " " + healthyPercent + " " + sleepPercent);

        return nextState;
    }

    private static boolean isStateMatch(
            MongState mongState, Double weightPercent, Double strengthPercent, Double satietyPercent, Double healthyPercent, Double sleepPercent) {
        return weightPercent < mongState.getWeightPercent() ||
                strengthPercent < mongState.getStrengthPercent() ||
                satietyPercent < mongState.getSatietyPercent() ||
                healthyPercent < mongState.getHealthyPercent() ||
                sleepPercent < mongState.getSleepPercent();
    }

    public static String getNextMongCode() {
        return getNextMongCode(MongGrade.EMPTY, "CH444");
    }

    public static String getNextMongCode(MongGrade mongGrade, String mongCode) {

        String nextMongCode = mongCode;

        switch (mongGrade) {
            case EMPTY -> {
                nextMongCode = "CH000";     // 없음 -> 0
            }
            case ZERO -> {
                nextMongCode = "CH100";     // 0 -> 1
            }
            case FIRST -> {
                nextMongCode = "CH200";     // 1 -> 2
            }
            case SECOND -> {
                nextMongCode = "CH300";     // 2 -> 3
            }
            case THIRD, LAST -> {}          // 3 -> 졸업 준비
        }

        return nextMongCode;
    }

    public static MongShift getNextShiftCode(MongGrade mongGrade, MongShift mongShift) {

        MongShift nextShiftCode = mongShift;

        switch (mongGrade) {
            case ZERO, FIRST, SECOND -> {
                nextShiftCode = MongShift.NORMAL;
            }
            case THIRD -> {
                nextShiftCode = MongShift.GRADUATE_READY;
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
            case ZERO -> {
                nextStateCode = MongState.NORMAL;
            }
            case FIRST, SECOND, THIRD -> {}
            case LAST -> {
                nextStateCode = MongState.EMPTY;
            }
        }

        return nextStateCode;
    }
}
