package com.mongs.play.domain.mong.service;

import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.entity.MongLog;
import com.mongs.play.domain.mong.enums.MongLogCode;
import com.mongs.play.domain.mong.repository.MongLogRepository;
import com.mongs.play.domain.mong.repository.MongRepository;
import com.mongs.play.domain.mong.vo.MongVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongStatusService {

    private final MongRepository mongRepository;
    private final MongLogRepository mongLogRepository;

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo decreaseStatus(Long mongId, Double subWeight, Double subStrength, Double subSatiety, Double subHealthy, Double subSleep) {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .weight(mong.getWeight() - subWeight)
                .strength(mong.getStrength() - subStrength)
                .satiety(mong.getSatiety() - subSatiety)
                .healthy(mong.getHealthy() - subHealthy)
                .sleep(mong.getSleep() - subSleep)
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.DECREASE_STATUS;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%f:%f:%f:%f:%f", mongLogCode.message, subWeight, subStrength, subSatiety, subHealthy, subSleep))
                .build());

        log.info("[decreaseStatus] mongId: {}, subWeight: {}, subStrength: {}, subSatiety: {}, subHealthy: {}, subSleep: {}", mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo increasePoopCount(Long mongId, Integer addPoopCount) {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .poopCount(mong.getPoopCount() + addPoopCount)
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.INCREASE_POOP_COUNT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, addPoopCount))
                .build());

        log.info("[increasePoopCount] mongId: {}, addPoopCount: {}", mongId, addPoopCount);

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo increaseStatus(Long mongId, Double addWeight, Double addStrength, Double addSatiety, Double addHealthy, Double addSleep) {

        Mong mong = mongRepository.findByIdAndIsActiveTrueWithLock(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        mong = mongRepository.save(mong.toBuilder()
                .weight(mong.getWeight() + addWeight)
                .strength(mong.getStrength() + addStrength)
                .satiety(mong.getSatiety() + addSatiety)
                .healthy(mong.getHealthy() + addHealthy)
                .sleep(mong.getSleep() + addSleep)
                .build().validation());

        MongLogCode mongLogCode = MongLogCode.INCREASE_STATUS;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%f:%f:%f:%f:%f", mongLogCode.message, addWeight, addStrength, addSatiety, addHealthy, addSleep))
                .build());

        log.info("[increaseStatus] mongId: {}, addWeight: {}, addStrength:{}, addSatiety: {}, addHealthy: {}, addSleep: {}", mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);

        return MongVo.of(mong);
    }
}
