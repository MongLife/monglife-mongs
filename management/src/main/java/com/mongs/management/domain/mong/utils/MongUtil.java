package com.mongs.management.domain.mong.utils;

import com.mongs.core.enums.management.MongGrade;
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
    public String getNextMongCode(Mong mong, MongGrade grade) {

        String nextMongCode = "CD444";

        switch (grade) {
            case ZERO -> {
                nextMongCode = "CD000";
            }
            case FIRST -> {
                nextMongCode = "CD100";
            }
            case SECOND -> {
                nextMongCode = "CD200";
            }
            case THIRD -> {
                nextMongCode = "CD300";
            }
            case FOURTH -> {
                nextMongCode = mong.getMongCode();
            }
        }

        return nextMongCode;
    }

}
