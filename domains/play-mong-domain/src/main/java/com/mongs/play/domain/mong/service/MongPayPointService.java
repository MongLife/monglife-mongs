package com.mongs.play.domain.mong.service;

import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongPayPointService {

    private final MongRepository mongRepository;

    public Mong increasePayPoint(Long mongId, Integer payPoint) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        return mongRepository.save(mong.toBuilder()
                .payPoint(mong.getPayPoint() + payPoint)
                .build());
    }

    public Mong decreasePayPoint(Long mongId, Integer payPoint) throws NotFoundException {

        Mong mong = mongRepository.findByIdAndIsActiveTrue(mongId)
                .orElseThrow(() -> new NotFoundException(MongErrorCode.NOT_FOUND_MONG));

        return mongRepository.save(mong.toBuilder()
                .payPoint(mong.getPayPoint() - payPoint)
                .build());
    }
}
