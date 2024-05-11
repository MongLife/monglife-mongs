package com.mongs.play.domain.mong.service;

import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.mong.annotation.MongStateValidation;
import com.mongs.play.domain.mong.annotation.MongStatusValidation;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.entity.MongLog;
import com.mongs.play.domain.mong.enums.MongLogCode;
import com.mongs.play.domain.mong.repository.MongLogRepository;
import com.mongs.play.domain.mong.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongStatusService {

    private final MongRepository mongRepository;
    private final MongLogRepository mongLogRepository;

    @MongStatusValidation
    @MongStateValidation
    public Mong decreaseWeight(Long mongId, Double subWeight) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .weight(mong.getWeight() - subWeight)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_WEIGHT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, subWeight))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong decreaseStrength(Long mongId, Double subStrength) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .strength(mong.getStrength() - subStrength)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_STRENGTH;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, subStrength))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong decreaseSatiety(Long mongId, Double subSatiety) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .satiety(mong.getSatiety() - subSatiety)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_SATIETY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, subSatiety))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong decreaseHealthy(Long mongId, Double subHealthy) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .healthy(mong.getHealthy() - subHealthy)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_HEALTHY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, subHealthy))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong decreaseSleep(Long mongId, Double subSleep) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .sleep(mong.getSleep() - subSleep)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_SLEEP;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, subSleep))
                .build());

        return mong;
    }

    public Mong decreasePoopCount(Long mongId, Integer subPoopCount) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .poopCount(mong.getPoopCount() - subPoopCount)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_POOP_COUNT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, subPoopCount))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseWeight(Long mongId, Double addWeight) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .weight(mong.getWeight() + addWeight)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_WEIGHT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, addWeight))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseStrength(Long mongId, Double addStrength) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .strength(mong.getStrength() + addStrength)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_STRENGTH;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, addStrength))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseSatiety(Long mongId, Double addSatiety) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .satiety(mong.getSatiety() + addSatiety)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_SATIETY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, addSatiety))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseHealthy(Long mongId, Double addHealthy) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .healthy(mong.getHealthy() + addHealthy)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_HEALTHY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, addHealthy))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseSleep(Long mongId, Double addSleep) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .sleep(mong.getSleep() + addSleep)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_SLEEP;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, addSleep))
                .build());

        return mong;
    }

    public Mong increasePoopCount(Long mongId, Integer addPoopCount) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .poopCount(mong.getPoopCount() + addPoopCount)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_POOP_COUNT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, addPoopCount))
                .build());

        return mong;
    }
}
