package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.repository.MongRepository;
import com.mongs.management.domain.mong.service.dto.*;
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
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MongServiceImpl implements MongService {

    private final MongRepository mongRepository;


    @Retryable(value = DataAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Override
    public CreateMong createMong(InitMong initMong, Long memberId) {
        String sleepTimeStart = timeConverter(initMong.sleepStart());
        String sleepTimeEnd = timeConverter(initMong.sleepEnd());
        Boolean sleep = isSleep(sleepTimeStart, sleepTimeEnd);

        Mong mong = Mong.builder()
                .memberId(memberId)
                .name(initMong.name())
                .sleepTime(sleepTimeStart)
                .wakeUpTime(sleepTimeEnd)
                .weight(new Random().nextDouble() * 100)
                .isSleeping(sleep)
                .build();
        mongRepository.save(mong);
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
        Mong mong = getMong(memberId);
        mong.doStroke(mong.getNumberOfStroke());
        return Stroke.of(mong);
    }

    @Override
    public Sleep toCheckMongsLifetime(Long memberId) {
        Mong mong = getMong(memberId);
        mong.changeSleepCondition(isSleep(mong.getSleepTime(), mong.getWakeUpTime()));
        return Sleep.of(mong);
    }

    // Poop Clean -> 0으로? or 1로?
    @Override
    public Poop toCleanMongsPoop(Long memberId) {
        Mong mong = getMong(memberId);
        mong.setNumberOfPoop(0);
        return Poop.of(mong);
    }

    @Override
    public EatTheFeed feedToMong(FeedCode feedCode, Long memberId) {
        return null;
    }

    @Override
    public Training mongTraining(TrainingCount trainingCount, Long memberId) {
        Mong mong = getMong(memberId);
        mong.setNumberOfTraining(trainingCount.getTrainingCount());
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

    @Override
    public List<FoodList> foodCategory(String foodCategory) {
        return null;
    }

    @Override
    public List<SlotList> slotInfo(Long memberId) {

        return null;
    }

    @Override
    public LastFoodBuyDate foodLastBusied(String foodCode, Long memberId) {
        return null;
    }


    private Mong getMong(Long memberId) {
        return mongRepository.findManagementByMemberId(memberId)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND));
    }
}
