package com.monglife.mongs.app.activity.training.service;

import com.monglife.mongs.app.activity.training.config.TrainingProperties;
import com.monglife.mongs.app.activity.training.enums.TrainingCode;
import com.monglife.mongs.app.activity.training.vo.TrainingEndVo;
import com.monglife.mongs.app.activity.training.vo.TrainingVo;
import com.monglife.mongs.client.manager.service.ManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingProperties trainingProperties;

    private final ManagementService managementService;

    /**
     * 훈련 정보 조회
     * @return 훈련 달성 스코어, 보상 페이 포인트 조회
     */
    public TrainingVo getTraining(TrainingCode trainingCode) {

        Integer rewardPayPoint = 0;
        Integer score = 0;
        Integer timeout = 0;

        switch (trainingCode) {
            case RUNNER -> {
                rewardPayPoint = trainingProperties.runner.rewardPayPoint;
                score = trainingProperties.runner.score;
                timeout = trainingProperties.runner.timeout;
            }

            case BASKETBALL -> {
                rewardPayPoint = trainingProperties.basketball.rewardPayPoint;
                score = trainingProperties.basketball.score;
                timeout = trainingProperties.basketball.timeout;
            }
        }

        return TrainingVo.builder()
                .rewardPayPoint(rewardPayPoint)
                .score(score)
                .timeout(timeout)
                .build();
    }

    /**
     * 훈련 달리기 완료
     * 불가능 상태 : 죽음, 알
     * @param mongId 몽 ID
     * @param score 점수
     */
    @Transactional
    public TrainingEndVo trainingRunnerEnd(Long mongId, Integer score) {

        boolean isSuccess = score >= trainingProperties.runner.score;
        int rewardPayPoint = trainingProperties.runner.rewardPayPoint;

        managementService.patchMongAfterTraining(
                mongId,
                trainingProperties.runner.exp,
                trainingProperties.runner.weight,
                trainingProperties.runner.strength,
                trainingProperties.runner.satiety,
                trainingProperties.runner.healthy,
                trainingProperties.runner.fatigue,
                trainingProperties.runner.poopCount,
                isSuccess ? rewardPayPoint : 0
        );

        return TrainingEndVo.builder()
                .isSuccess(isSuccess)
                .rewardPayPoint(rewardPayPoint)
                .score(score)
                .build();
    }

    /**
     * 훈련 농구 완료
     * @param mongId 몽 ID
     */
    @Transactional
    public TrainingEndVo trainingBasketballEnd(Long mongId, Integer score) {

        boolean isSuccess = score >= trainingProperties.basketball.score;
        int rewardPayPoint = trainingProperties.basketball.rewardPayPoint;

        managementService.patchMongAfterTraining(
                mongId,
                trainingProperties.basketball.exp,
                trainingProperties.basketball.weight,
                trainingProperties.basketball.strength,
                trainingProperties.basketball.satiety,
                trainingProperties.basketball.healthy,
                trainingProperties.basketball.fatigue,
                trainingProperties.basketball.poopCount,
                isSuccess ? rewardPayPoint : 0
        );

        return TrainingEndVo.builder()
                .isSuccess(isSuccess)
                .rewardPayPoint(rewardPayPoint)
                .score(score)
                .build();
    }
}
