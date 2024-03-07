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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MongServiceImpl implements MongService {

    private final MongRepository mongRepository;
    private final AteFoodHistoryRepository ateFoodHistoryRepository;

    @Value("${value.traing_paied}")
    private int traingPaiedPoint;


    // 몽생성
    @Override
    public CreateMong createMong(InitMong initMong, Long memberId) {
        log.info("time ={}", initMong.sleepStart());
        String sleepTimeStart = timeConverter(initMong.sleepStart());
        String sleepTimeEnd = timeConverter(initMong.sleepEnd());
        Boolean sleep = isSleep(sleepTimeStart, sleepTimeEnd);

        // 몽 이름, 잠 (기상, 취침) -> 이거에 따라서 자는지 안자는지 체크
        Mong mong = Mong.builder()
                .memberId(memberId)
                .name(initMong.name())
                .sleepTime(sleepTimeStart)
                .wakeUpTime(sleepTimeEnd)
                .isSleeping(sleep)
                .build();

        mongRepository.save(mong);
        return CreateMong.of(mong);

    }

    // 자는지 확인하는 메서드
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

    // HH:mm로 변경하기 위한 컨버터
    private String timeConverter(LocalDateTime time) {
        log.info("time ={}", time);
        return time.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    // 몽 쓰다듬기 -> 경험치 상승, 몽 쓰다듬 횟수 + 1
    @Override
    public Stroke toMongStroke(Long memberId) {
        Mong mong = getMong(memberId);
        mong.doStroke(mong.getNumberOfStroke());
        mong.setExp(mong.getExp() + MongEXP.STROKE.getExp());
        return Stroke.of(mong);
    }

    // 몽 낮잠 3시간 뒤에는 일어나야한다. -> 스케줄러
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

    // 몽 먹이주기, 단백질 음식 먹으면 근력 향상 ( 코드에 있는 FD 뒤에 있는 숫자 / 5 해서 진행 )
    @Override
    public EatTheFeed feedToMong(FeedCode feed, Long memberId) {
        Mong mong = getMong(memberId);
        InitFoodCodeData[] values = InitFoodCodeData.values();
        InitFoodCodeData food = null;

        for (InitFoodCodeData value : values) {
            if(value.getCode().equals(feed.feedCode())) {
                if(value.getGroupCode().equals("FD")) {
                    String[] strings = value.getName().split("FD");
                    int parseInt = Integer.parseInt(strings[1]) / 5;
                    mong.setStrength(mong.getStrength() + parseInt);
                }
                mong.setSatiety(mong.getSatiety() + value.getFullness());
                mong.setPoint(mong.getPaypoint() - value.getPoint());
                food = value;
                break;
            }
        }

        if(food == null) {
            throw new ManagementException(ManagementErrorCode.NOT_FOUND_FOOD_CODE);
        }

        ateFoodHistoryRepository.save(AteFoodHistory.builder()
                        .foodName(food.getName())
                        .mong(mong)
                        .build());
        mong.setExp(mong.getExp() + MongEXP.EAT_THE_FOOD.getExp());
        return EatTheFeed.of(mong, food);
    }


    // 몽 훈련 ( paypoint가 traingPaiedPoint (50) 보다 낮으면 훈련 불가 )
    // 포인트 감소, 훈련 횟수, 근력, 경험치 증가
    @Override
    public Training mongTraining(TrainingCount trainingCount, Long memberId) {
        Mong mong = getMong(memberId);
        if(mong.getPaypoint() <= traingPaiedPoint) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_PAYPOINT);
        }
        mong.setPoint(mong.getPaypoint() - traingPaiedPoint);
        mong.setNumberOfTraining(trainingCount.getTrainingCount());
        mong.setStrength(mong.getStrength() + 5);
        mong.setExp(mong.getExp() + MongEXP.TRAINING.getExp());
        return Training.of(mong);
      
    }

    // 몽 진화 MongEvolutionEXP를 순회하면서 체크
    @Override
    public Evolution mongEvolution(Long memberId) {
        Mong mong = getMong(memberId);
        int exp = mong.getExp();
        MongEvolutionEXP[] values = MongEvolutionEXP.values();
        boolean flag = false;
        for (int i = values.length - 1; i >= 0; i--) {
            if (exp >= Integer.parseInt(values[i].getExp())) {
                mong.setGrade(values[i].getName());
                flag = true;
                break;
            }
        }

        if(!flag) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }
      
        return Evolution.builder()
                .mongCode(mong.getGrade().getCode())
                .stateCode(mong.getCondition().getCode())
                .build();
    }


    // 몽 졸업 ( 4단계 달성하면 졸업 )
    @Override
    public Graduation mongsGraduate(Long memberId) {
        Mong mong = getMong(memberId);
        if(!mong.getGrade().getName().equals(MongEvolutionEXP.FOURTH_GRADE.getName())) {
            throw new ManagementException(ManagementErrorCode.NOT_ENOUGH_EXP);
        }
        return Graduation.of(mong);
    }

    private Mong getMong(Long memberId) {
        return mongRepository.findManagementByMemberId(memberId)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND));
    }
}
