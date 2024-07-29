package com.mongs.play.domain.mong.enums;

import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MongTrainingCode {

    JUMPING("TR100", "Jumping Training", 0, 2, 2, -2D, 2D, -1.5D, 0D, -0.5D)
    ;

    public final String code;
    public final String name;
    public final Integer point;
    public final Integer rewardPoint;
    public final Integer exp;
    public final Double addWeightValue;
    public final Double addStrengthValue;
    public final Double addSatietyValue;
    public final Double addHealthyValue;
    public final Double addSleepValue;

    public static MongTrainingCode findMongTrainingCode(String code) throws NotFoundException {
        try {
            return MongTrainingCode.valueOf(code);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(MongErrorCode.NOT_FOUND_TRAINING_CODE);
        }
    }
}
