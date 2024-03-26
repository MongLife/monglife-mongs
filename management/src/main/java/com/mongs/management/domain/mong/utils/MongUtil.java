package com.mongs.management.domain.mong.utils;

import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.management.domain.mong.entity.Mong;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class MongUtil {

    // 자는지 확인하는 메서드
    public Boolean isSleep(String sleepStart, String sleepEnd) {
        LocalTime startTime = LocalTime.parse(sleepStart, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(sleepEnd, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime currentTime = LocalTime.now();

        if (endTime.isBefore(startTime)) {
            return !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime);
        } else {
            return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
        }
    }

    // HH:mm로 변경하기 위한 컨버터
    public String timeConverter(LocalDateTime time) {
        return time.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

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

    public Double statusToPercent(Double status, MongGrade mongGrade) {
        double maxStatus = mongGrade.getMaxStatus();

        if (maxStatus == 0D) {
            return 0D;
        }

        return status / maxStatus * 100;
    }
}
