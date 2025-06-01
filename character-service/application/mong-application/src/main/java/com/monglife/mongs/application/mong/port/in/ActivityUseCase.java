package com.monglife.mongs.application.mong.port.in;

import com.monglife.mongs.application.mong.port.in.command.GetTrainingTypeCommand;
import com.monglife.mongs.application.mong.port.in.command.TrainingEndCommand;
import com.monglife.mongs.domain.mong.model.Mong;
import com.monglife.mongs.domain.mong.model.TrainingType;

import java.util.List;

public interface ActivityUseCase {

    /**
     * 훈련 타입 목록 조회
     */
    List<TrainingType> getTrainingTypesUseCase();

    /**
     * 훈련 타입 조회
     */
    TrainingType getTrainingTypeUseCase(GetTrainingTypeCommand command);

    /**
     * 훈련 완료
     */
    Mong trainingEndUseCase(TrainingEndCommand command);
}
