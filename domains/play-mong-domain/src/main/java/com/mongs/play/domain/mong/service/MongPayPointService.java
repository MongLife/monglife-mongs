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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongPayPointService {

    private final MongRepository mongRepository;
    private final MongLogRepository mongLogRepository;

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo increasePayPoint(Long mongId, Integer addPayPoint) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        int payPoint = mong.getPayPoint() + addPayPoint;

        mong = mongRepository.save(mong.toBuilder()
                .payPoint(payPoint)
                .build());

        MongLogCode mongLogCode = MongLogCode.INCREASE_PAY_POINT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%d", mongLogCode.message, addPayPoint))
                .build());

        log.info("[increasePayPoint] mongId: {}, addPayPoint: {}]", mong.getId(), addPayPoint);

        return MongVo.of(mong);
    }

    @Transactional(transactionManager = "mongTransactionManager")
    public MongVo decreasePayPoint(Long mongId, Integer subPayPoint) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_ACTIVE_MONG));

        int payPoint = mong.getPayPoint() - subPayPoint;

        mong = mongRepository.save(mong.toBuilder()
                .payPoint(mong.getPayPoint() - subPayPoint)
                .build());

        MongLogCode mongLogCode = MongLogCode.DECREASE_PAY_POINT;
        mongLogRepository.save(MongLog.builder()
                .mongId(mong.getId())
                .mongLogCode(mongLogCode)
                .message(String.format("%s:%s", mongLogCode.message, subPayPoint))
                .build());

        log.info("[decreasePayPoint] mongId: {}, subPayPoint: {}]", mong.getId(), subPayPoint);

        return MongVo.of(mong);
    }
}
