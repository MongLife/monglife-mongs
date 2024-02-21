package com.mongs.management.domain.service;

import com.mongs.core.error.ErrorException;
import com.mongs.management.domain.entity.Management;
import com.mongs.management.domain.repository.ManagementRepository;
import com.mongs.management.domain.service.dto.*;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagementServiceImpl implements ManagementService {

    private final ManagementRepository managementRepository;


    @Override
    public CreateMong createMong(InitMong initmong, Long memberId) {
        return null;
    }

    @Override
    public Stroke toMongStroke(Long memberId) {
        return null;
    }

    @Override
    public Sleep toCheckMongsLifetime(Long memberId) {
        return null;
    }

    @Override
    public Poop toCleanMongsPoop(Long memberId) {
        return null;
    }

    @Override
    public EatTheFeed feedToMong(FeedCode feedCode, Long memberId) {
        return null;
    }

    @Override
    public Training mongTraining(TrainingCount trainingCount, Long memberId) {
        return null;
    }

    @Override
    public Evolution mongEvolution(Long memberId) {
        return null;
    }

    @Override
    public Graduation mongsGraduate(Long memberId) {
        return null;
    }

}
