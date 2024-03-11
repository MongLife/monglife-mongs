package com.mongs.management.domain.mong.service.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongEXP {

    STROKE(10, "10EXP per stroke"),
    TRAINING(20, "20EXP per training"),
    ATTENDANCE(5, "5EXP per attendance"),
    CLEANING_POOP(5, "5EXP per attendance"),
    EAT_THE_FOOD(10, "5EXP per attendance"),
    NAP(5, "5EXP per nap");

    private final Integer exp;
    private final String name;
}
