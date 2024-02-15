package com.mongs.management.management.service;

import com.mongs.management.management.entity.Management;
import com.mongs.management.management.repository.ManagementRepository;
import com.mongs.management.management.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagementServiceImpl implements ManagementService {

    private final ManagementRepository managementRepository;

    @Override
    public CreateMong createMong(InitMong initmong) {
        return null;
    }

    @Override
    public Stroke toMongStroke() {
        return null;
    }

    @Override
    public Sleep toCheckMongsLifetime() {
        return null;
    }

    @Override
    public Poop toCleanMongsPoop() {
        return null;
    }

    @Override
    public EatTheFeed feedToMong(FeedCode feedCode) {
        return null;
    }

    @Override
    public Training mongTraining(TrainingCount trainingCount) {
        return null;
    }

    @Override
    public Evolution mongEvolution() {
        return null;
    }

    @Override
    public Graduation mongsGraduate() {
        return null;
    }

}
