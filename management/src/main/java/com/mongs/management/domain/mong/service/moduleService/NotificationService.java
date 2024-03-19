package com.mongs.management.domain.mong.service.moduleService;


import com.mongs.core.vo.mqtt.*;

public interface NotificationService {
    void publishCreate(Long accountId, PublishCreateVo publishCreateVo);
    void publishDelete(Long accountId, PublishDeleteVo publishDeleteVo);
    void publishStroke(Long accountId, PublishStrokeVo publishStrokeVo);
    void publishFeed(Long accountId, PublishFeedVo publishFeedVo);
    void publishSleeping(Long accountId, PublishSleepingVo publishSleepingVo);
    void publishPoop(Long accountId, PublishPoopVo publishPoopVo);
    void publishTraining(Long accountId, PublishTrainingVo publishTrainingVo);
    void publishGraduation(Long accountId, PublishGraduationVo publishGraduationVo);
    void publishEvolution(Long accountId, PublishEvolutionVo publishEvolutionVo);
    void publishEvolutionReady(Long accountId, PublishEvolutionReadyVo publishEvolutionReadyVo);
}
