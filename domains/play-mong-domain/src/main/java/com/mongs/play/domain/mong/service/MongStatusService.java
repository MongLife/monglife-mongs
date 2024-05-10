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
    public Mong decreaseWeight(Long mongId, Double weight) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .weight(mong.getWeight() - weight)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_WEIGHT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", weight, mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong decreaseStrength(Long mongId, Double strength) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .strength(mong.getStrength() - strength)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_STRENGTH;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", strength, mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong decreaseSatiety(Long mongId, Double satiety) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .satiety(mong.getSatiety() - satiety)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_SATIETY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", satiety, mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong decreaseHealthy(Long mongId, Double healthy) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .healthy(mong.getHealthy() - healthy)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_HEALTHY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", healthy, mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong decreaseSleep(Long mongId, Double sleep) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .sleep(mong.getSleep() - sleep)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_SLEEP;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", sleep, mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseWeight(Long mongId, Double weight) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .weight(mong.getWeight() + weight)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_WEIGHT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", weight, mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseStrength(Long mongId, Double strength) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .strength(mong.getStrength() + strength)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_STRENGTH;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", strength, mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseSatiety(Long mongId, Double satiety) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .satiety(mong.getSatiety() + satiety)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_SATIETY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", satiety, mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseHealthy(Long mongId, Double healthy) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .healthy(mong.getHealthy() + healthy)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_HEALTHY;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", healthy, mongLogCode.message))
                .build());

        return mong;
    }

    @MongStatusValidation
    @MongStateValidation
    public Mong increaseSleep(Long mongId, Double sleep) {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .sleep(mong.getSleep() + sleep)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_SLEEP;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s 만큼 %s", sleep, mongLogCode.message))
                .build());

        return mong;
    }
}
