package com.monglife.mongs.application.mong.port.in;

import com.monglife.mongs.application.mong.port.in.command.GetTrainingTypeCommand;
import com.monglife.mongs.application.mong.port.in.command.TrainingEndCommand;
import com.monglife.mongs.domain.mong.model.Mong;
import com.monglife.mongs.domain.mong.model.TrainingType;

import java.util.List;

public interface ActivityUseCase {

    List<TrainingType> getTrainingTypesUseCase();

    TrainingType getTrainingTypeUseCase(GetTrainingTypeCommand command);

    Mong trainingEndUseCase(TrainingEndCommand command);
}
