package com.mongs.lifecycle.utils;

import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.lifecycle.entity.Mong;
import org.springframework.stereotype.Component;

@Component
public class MongUtil {

    public String getNextMongCode(Mong mong) {

        String nextMongCode = "CH444";

        switch (mong.getGrade()) {
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
            case THIRD, LAST -> {
                nextMongCode = mong.getMongCode();
            }
        }

        return nextMongCode;
    }

    public MongShift getNextShiftCode(Mong mong) {

        MongShift nextShiftCode = MongShift.EMPTY;

        switch (mong.getGrade()) {
            case ZERO, FIRST, SECOND, THIRD -> {
                nextShiftCode = MongShift.NORMAL;
            }
            case LAST -> {
                nextShiftCode = MongShift.DELETE;
            }
        }

        return nextShiftCode;
    }

    public MongState getNextStateCode(Mong mong) {

        MongState nextStateCode = MongState.EMPTY;

        switch (mong.getGrade()) {
            case ZERO, LAST -> {
                nextStateCode = MongState.NORMAL;
            }
            case FIRST, SECOND, THIRD -> {
                nextStateCode = mong.getState();
            }
        }

        return nextStateCode;
    }

    public Double stateToPercent(Double status, MongGrade mongGrade) {
        double maxStatus = mongGrade.getMaxStatus();

        return status / maxStatus * 100;
    }
}
