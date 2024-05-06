package com.mongs.play.domain.mong.service;

import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.domain.NotFoundException;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongPayPointService {

    private final MongRepository mongRepository;

    public Mong modifyIncreasePayPoint(Long mongId, Integer payPoint) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        return mongRepository.save(mong.toBuilder()
                .payPoint(mong.getPayPoint() + payPoint)
                .build());
    }

    public Mong modifyDecreasePayPoint(Long mongId, Integer payPoint) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        return mongRepository.save(mong.toBuilder()
                .payPoint(mong.getPayPoint() - payPoint)
                .build());
    }
}
