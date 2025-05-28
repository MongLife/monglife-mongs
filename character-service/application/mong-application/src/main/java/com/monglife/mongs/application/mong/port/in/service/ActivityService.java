package com.monglife.mongs.application.mong.port.in.service;

import com.monglife.mongs.application.mong.port.annotation.CheckMongDead;
import com.monglife.mongs.application.mong.port.annotation.PublishMongPort;
import com.monglife.mongs.application.mong.port.exception.NotExistsMongException;
import com.monglife.mongs.application.mong.port.exception.NotExistsTrainingTypeException;
import com.monglife.mongs.application.mong.port.in.ActivityUseCase;
import com.monglife.mongs.application.mong.port.in.command.GetTrainingTypeCommand;
import com.monglife.mongs.application.mong.port.in.command.TrainingEndCommand;
import com.monglife.mongs.application.mong.port.out.MongPersistencePort;
import com.monglife.mongs.application.mong.port.out.MongReadPort;
import com.monglife.mongs.domain.mong.model.Mong;
import com.monglife.mongs.domain.mong.model.TrainingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService  implements ActivityUseCase {

    private final MongPersistencePort mongPersistencePort;

    private final MongReadPort mongReadPort;

    /**
     * 훈련 타입 목록 조회
     */
    @Override
    @Transactional
    public List<TrainingType> getTrainingTypesUseCase() {
        return mongReadPort.getTrainingTypesPort();
    }

    /**
     * 훈련 타입 조회
     */
    @Override
    @Transactional
    public TrainingType getTrainingTypeUseCase(GetTrainingTypeCommand command) {
        return mongReadPort.getTrainingTypePort(command.getTrainingCode())
                .orElseThrow(NotExistsTrainingTypeException::new);
    }

    /**
     * 훈련 완료
     */
    @Override
    @Transactional
    @CheckMongDead
    @PublishMongPort
    public Mong trainingEndUseCase(TrainingEndCommand command) {

        // 훈련 타입 조회
        TrainingType trainingType = mongReadPort.getTrainingTypePort(command.getTrainingCode())
                .orElseThrow(NotExistsTrainingTypeException::new);

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 스코어 달성 시 페이 포인트 증가
        if (trainingType.getScore() <= command.getScore()) {
            mong.trainingWithReward(trainingType);
        } else {
            mong.training(trainingType);
        }

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        return mong;
    }
}
