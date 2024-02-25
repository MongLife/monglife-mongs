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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ManagementServiceImpl implements ManagementService {

    private final ManagementRepository managementRepository;


    @Retryable(value = DataAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Override
    public CreateMong createMong(InitMong initMong, Long memberId) {
        String sleepTimeStart = timeConverter(initMong.sleepStart());
        String sleepTimeEnd = timeConverter(initMong.sleepEnd());
        Boolean sleep = isSleep(sleepTimeStart, sleepTimeEnd);

        Management mong = Management.builder()
                .memberId(memberId)
                .name(initMong.name())
                .sleepStart(sleepTimeStart)
                .sleepEnd(sleepTimeEnd)
                .weight(new Random().nextDouble() * 100)
                .sleep(sleep)
                .build();
        managementRepository.save(mong);
        return CreateMong.of(mong);

    }

    private Boolean isSleep(String sleepStart, String sleepEnd) {
        LocalTime startTime = LocalTime.parse(sleepStart, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(sleepEnd, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime currentTime = LocalTime.now();

        if (endTime.isBefore(startTime)) {
            return !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime);
        } else {
            return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
        }
    }

    private String timeConverter(LocalDateTime time) {
        return time.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public Stroke toMongStroke(Long memberId) {
        Management mong = getMong(memberId);
        mong.doStroke(mong.getStrokeCount());
        return Stroke.of(mong);
    }

    @Override
    public Sleep toCheckMongsLifetime(Long memberId) {
        Management mong = getMong(memberId);
        mong.changeSleepConditon(isSleep(mong.getSleepStart(), mong.getSleepEnd()));
        return Sleep.of(mong);
    }

    // Poop Clean -> 0으로? or 1로?
    @Override
    public Poop toCleanMongsPoop(Long memberId) {
        Management mong = getMong(memberId);
        mong.setPoopCount(0);
        return Poop.of(mong);
    }

    @Override
    public EatTheFeed feedToMong(FeedCode feedCode, Long memberId) {
        return null;
    }

    @Override
    public Training mongTraining(TrainingCount trainingCount, Long memberId) {
        Management mong = getMong(memberId);
        mong.setTrainingCount(trainingCount.getTrainingCount());
        return Training.of(mong);
    }

    @Override
    public Evolution mongEvolution(Long memberId) {
        return null;
    }

    @Override
    public Graduation mongsGraduate(Long memberId) {
        return null;
    }

    private Management getMong(Long memberId) {
        return managementRepository.findManagementByMemberId(memberId)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND));
    }
}
