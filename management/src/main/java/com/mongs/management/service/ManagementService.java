package com.mongs.management.service;

import com.mongs.management.service.dto.*;

public interface ManagementService {

    CreateMong createMong (InitMong initmong);
    Stroke toMongStroke ();
    Sleep toCheckMongsLifetime ();
    Poop toCleanMongsPoop ();
    EatTheFeed feedToMong (FeedCode feedCode);
    Training mongTraining (TrainingCount trainingCount);
    Evolution mongEvolution ();
    Graduation mongsGraduate();
}
