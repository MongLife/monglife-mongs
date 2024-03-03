package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.repository.MongRepository;
import com.mongs.management.domain.mong.service.dto.*;
import com.mongs.management.domain.mong.service.enums.MongEvolutionEXP;
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
        mong.changeSleepCondition(mong.getIsSleeping());
        return Sleep.of(mong);
    }

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
        if(mong.getPaypoint() >= 50) {
            mong.setNumberOfTraining(trainingCount.getTrainingCount());
            return Training.of(mong);
        }
        throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_PAYPOINT);
    }

    @Override
    public Evolution mongEvolution(Long memberId) {
        Mong mong = getMong(memberId);
        int exp = mong.getExp();
        MongEvolutionEXP[] values = MongEvolutionEXP.values();
        for (int i = values.length - 1; i >= 0; i--) {
            if (exp >= Integer.parseInt(values[i].getExp())) {
                mong.setGrade(values[i].getName());
                break;
            }
        }
        return Evolution.builder()
                .mongCode(mong.getGrade().getCode())
                .stateCode(mong.getCondition().getCode())
                .build();
    }

    @Override
    public Graduation mongsGraduate(Long memberId) {
        Mong mong = getMong(memberId);
        if(!mong.getGrade().getName().equals(MongEvolutionEXP.FOURTH_GRADE.getName())) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }
        return Graduation.builder()
                .mongCode(mong.getGrade().getCode())
                .build();
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
