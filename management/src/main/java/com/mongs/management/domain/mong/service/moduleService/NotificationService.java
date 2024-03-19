package com.mongs.management.domain.mong.service.moduleService;


import com.mongs.management.domain.mong.entity.Mong;

public interface NotificationService {
    void publishCreate(Mong mong);
    void publishDelete(Mong mong);
    void publishStroke(Mong mong);
    void publishFeed(Mong mong);
    void publishSleeping(Mong mong);
    void publishPoop(Mong mong);
    void publishTraining(Mong mong);
    void publishGraduation(Mong mong);
    void publishEvolution(Mong mong);
    void publishEvolutionReady(Mong mong);
}
