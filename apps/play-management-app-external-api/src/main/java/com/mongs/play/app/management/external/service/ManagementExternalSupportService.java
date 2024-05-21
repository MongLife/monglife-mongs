package com.mongs.play.app.management.external.service;

import com.mongs.play.client.publisher.mong.annotation.RealTimeMong;
import com.mongs.play.client.publisher.mong.code.PublishCode;
import com.mongs.play.domain.code.entity.MongCode;
import com.mongs.play.domain.code.service.CodeService;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.vo.MongVo;
import com.mongs.play.module.feign.service.ManagementInternalFeignService;
import com.mongs.play.module.feign.service.ManagementWorkerFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementExternalSupportService {
    private final Random random = new Random();

    private final CodeService codeService;
    private final MongService mongService;
    private final ManagementWorkerFeignService managementWorkerFeignService;

    @RealTimeMong(codes = { PublishCode.MONG_SHIFT })
    @Transactional
    public MongVo evolutionReady(Long mongId) {
        return mongService.toggleEvolutionReady(mongId);
    }

    @Transactional
    public MongVo firstEvolution(MongVo mongVo) {
        List<MongCode> mongCodeList = codeService.getMongCodeByLevel(MongGrade.ZERO.nextGrade.level);
        int randIdx = random.nextInt(mongCodeList.size());
        String mongCode = mongCodeList.get(randIdx).code();

        MongVo newMongVo = mongService.toggleFirstEvolution(mongVo.mongId(), mongCode);

        managementWorkerFeignService.firstEvolutionSchedule(mongVo.mongId());

        return newMongVo;
    }

    @Transactional
    public MongVo evolution(MongVo mongVo) {
        // TODO("진화 포인트 환산")
        int evolutionPoint = 0;

        List<MongCode> mongCodeList = codeService.getMongCodeByLevelAndEvolutionPoint(mongVo.grade().nextGrade.level, evolutionPoint);

        // TODO("컬렉션 목록을 조회하여 겹치지 않도록 하는 로직 필요")

        String mongCode = mongCodeList.get(mongCodeList.size() - 1).code();

        MongVo newMongVo = mongService.toggleEvolution(mongVo.mongId(), mongCode);

        managementWorkerFeignService.evolutionSchedule(mongVo.mongId());

        return newMongVo;
    }

    @Transactional
    public MongVo lastEvolution(MongVo mongVo) {
        MongVo newMongVo = mongService.toggleLastEvolution(mongVo.mongId());
        managementWorkerFeignService.lastEvolutionSchedule(mongVo.mongId());
        return newMongVo;
    }
}
