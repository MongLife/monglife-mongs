package com.monglife.mongs.app.activity.training.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TrainingCode {

    RUNNER("달리기"),
    BASKETBALL("농구"),
    ;

    public final String name;

}
