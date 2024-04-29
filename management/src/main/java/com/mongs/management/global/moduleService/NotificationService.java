package com.mongs.management.global.moduleService;

import com.mongs.management.global.moduleService.vo.*;

public interface NotificationService {
    void publishCreate(Long accountId, PublishCreateVo publishCreateVo);
    void publishDelete(Long accountId, PublishDeleteVo publishDeleteVo);
    void publishStroke(Long accountId, PublishStrokeVo publishStrokeVo);
    void publishFeed(Long accountId, PublishFeedVo publishFeedVo);
    void publishSleeping(Long accountId, PublishSleepingVo publishSleepingVo);
    void publishPoop(Long accountId, PublishPoopVo publishPoopVo);
    void publishTraining(Long accountId, PublishTrainingVo publishTrainingVo);
    void publishGraduation(Long accountId, PublishGraduationVo publishGraduationVo);
    void publishGraduationReady(Long accountId, PublishGraduationReadyVo publishGraduationReadyVo);
    void publishEvolution(Long accountId, PublishEvolutionVo publishEvolutionVo);
    void publishEvolutionReady(Long accountId, PublishEvolutionReadyVo publishEvolutionReadyVo);
    void publishState(Long accountId, PublishStateVo publishStateVo);
    void publishAttendance(Long accountId, PublishAttendanceVo publishAttendanceVo);
}
