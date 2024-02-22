package com.mongs.management.domain.service;

import com.mongs.management.domain.entity.Management;
import com.mongs.management.domain.repository.ManagementRepository;
import com.mongs.management.domain.service.dto.*;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ManagementServiceImpl implements ManagementService {

    private final ManagementRepository managementRepository;


    @Retryable(value = DataAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Override
    public CreateMong createMong(InitMong initmong, Long memberId) {

        Boolean sleep = initmong.sleepEnd().isAfter(LocalDateTime.now()) &&
                initmong.sleepStart().isBefore(LocalDateTime.now());

        Management mong = Management.builder()
                .memberId(memberId)
                .name(initmong.name())
                .sleepStart(initmong.sleepStart())
                .sleepEnd(initmong.sleepEnd())
                .weight(new Random().nextDouble() * 100)
                .sleep(sleep)
                .build();
        managementRepository.save(mong);
        return CreateMong.of(mong);

    }

    @Override
    public Stroke toMongStroke(Long memberId) {
        Management mong = managementRepository.findManagementByMemberId(memberId)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND));

        mong.doStroke(mong.getStrokeCount());
        return Stroke.of(mong);
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
