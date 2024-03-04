package com.mongs.management.domain.mong.service;

import com.mongs.management.domain.food.InitFoodCodeData;
import com.mongs.management.domain.ateFood.AteFoodHistory;
import com.mongs.management.domain.ateFood.AteFoodHistoryRepository;
import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.repository.MongRepository;
import com.mongs.management.domain.mong.service.dto.*;
import com.mongs.management.domain.mong.service.enums.MongEXP;
import com.mongs.management.domain.mong.service.enums.MongEvolutionEXP;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AteFoodHistoryRepository ateFoodHistoryRepository;


    @Override
    public CreateMong createMong(InitMong initMong, Long memberId) {
        log.info("time ={}", initMong.sleepStart());
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
        log.info("time ={}", time);
        return time.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public Stroke toMongStroke(Long memberId) {
        Mong mong = getMong(memberId);
        mong.doStroke(mong.getNumberOfStroke());
        mong.setExp(mong.getExp() + MongEXP.STROKE.getExp());
        return Stroke.of(mong);
    }

    @Override
    public Sleep toCheckMongsLifetime(Long memberId) {
        Mong mong = getMong(memberId);
        mong.changeSleepCondition(mong.getIsSleeping());
        mong.setExp(mong.getExp() + MongEXP.NAP.getExp());
        return Sleep.of(mong);
    }

    @Override
    public Poop toCleanMongsPoop(Long memberId) {
        Mong mong = getMong(memberId);
        mong.setNumberOfPoop(0);
        mong.setExp(mong.getExp() + MongEXP.CLEANING_POOP.getExp());
        return Poop.of(mong);
    }

    @Override
    public EatTheFeed feedToMong(FeedCode feed, Long memberId) {
        Mong mong = getMong(memberId);
        InitFoodCodeData[] values = InitFoodCodeData.values();
        InitFoodCodeData data = null;
        for (InitFoodCodeData value : values) {
            if(value.getCode().equals(feed.feedCode())) {
                if(value.getGroupCode().equals("FD")) {
                    String[] strings = value.getName().split("FD");
                    int parseInt = Integer.parseInt(strings[1]) / 5;
                    mong.setStrength(mong.getStrength() + parseInt);
                }
                mong.setSatiety(mong.getSatiety() + value.getFullness());
                mong.setPoint(mong.getPaypoint() - value.getPoint());
                data = value;
                break;
            }
        }
        if(data == null) {
            throw new ManagementException(ManagementErrorCode.NOT_FOUND);
        }
        ateFoodHistoryRepository.save(AteFoodHistory.builder()
                        .foodName(data.getName())
                        .mong(mong)
                        .build());
        mong.setExp(mong.getExp() + MongEXP.EAT_THE_FOOD.getExp());
        return EatTheFeed.of(mong, data);
    }


    @Override
    public Training mongTraining(TrainingCount trainingCount, Long memberId) {
        Mong mong = getMong(memberId);
        if(mong.getPaypoint() >= 50) {
            mong.setNumberOfTraining(trainingCount.getTrainingCount());
            mong.setStrength(mong.getStrength() + 5);
            mong.setExp(mong.getExp() + MongEXP.TRAINING.getExp());
            return Training.of(mong);
        }
        throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_PAYPOINT);
    }

    @Override
    public Evolution mongEvolution(Long memberId) {
        Mong mong = getMong(memberId);
        int exp = mong.getExp();
        MongEvolutionEXP[] values = MongEvolutionEXP.values();
        boolean isDone = false;
        for (int i = values.length - 1; i >= 0; i--) {
            if (exp >= Integer.parseInt(values[i].getExp())) {
                mong.setGrade(values[i].getName());
                isDone = true;
                break;
            }
        }
        if(!isDone) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }
        return Evolution.builder()
                .mongCode(mong.getGrade().getCode())
                .stateCode(mong.getMongCondition().getCode())
                .weight(mong.getWeight())
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
    public List<SlotList> slotInfo(Long memberId) {
        return null;
    }

    private Mong getMong(Long memberId) {
        return mongRepository.findManagementByMemberId(memberId)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND));
    }
}
