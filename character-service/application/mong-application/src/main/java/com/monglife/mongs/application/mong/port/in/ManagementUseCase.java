package com.monglife.mongs.application.mong.port.in;

import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.domain.mong.model.Mong;

import java.util.List;

public interface ManagementUseCase {

    Mong createMongUseCase(CreateMongCommand command);

    Mong deleteMongUseCase(DeleteMongCommand command);

    Mong deadMongUseCase(DeadMongCommand command);

    List<Mong> getMongsUseCase(GetMongsCommand command);

    Mong getMongUseCase(GetMongCommand command);

    Mong strokeMongUseCase(StrokeMongCommand command);

    Mong sleepMongUseCase(SleepMongCommand command);

    Mong wakeUpMongUseCase(WakeupMongCommand command);

    Mong poopCleanMongUseCase(PoopCleanMongCommand command);

    Mong evolutionReadyMongUseCase(EvolutionReadyMongCommand command);

    Mong evolutionMongUseCase(EvolutionMongCommand command);

    Mong graduateMongUseCase(GraduateMongCommand command);

    Mong increaseMongPayPointUseCase(IncreaseMongPayPointCommand command);

    Mong increaseMongStatusUseCase(IncreaseMongStatusCommand command);

    Mong decreaseMongStatusUseCase(DecreaseMongStatusCommand command);

    Mong increaseMongPoopCountUseCase(IncreaseMongPoopCountCommand command);
}
