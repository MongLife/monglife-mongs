package com.mongs.play.app.management.external.service;

import com.mongs.play.app.management.external.vo.*;
import com.mongs.play.client.publisher.mong.annotation.RealTimeMong;
import com.mongs.play.client.publisher.mong.code.PublishCode;
import com.mongs.play.core.error.app.ManagementExternalErrorCode;
import com.mongs.play.core.error.domain.MongErrorCode;
import com.mongs.play.core.exception.app.ManagementExternalException;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.domain.code.entity.FoodCode;
import com.mongs.play.domain.code.entity.MongCode;
import com.mongs.play.domain.code.service.CodeService;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongTrainingCode;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.domain.mong.utils.MongUtil;
import com.mongs.play.domain.mong.vo.MongFeedLogVo;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongVo;
import com.mongs.play.module.kafka.annotation.SendCommit;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementExternalSupportService {
    private final Random random = new Random();

    private final CodeService codeService;
    private final MongService mongService;

    @RealTimeMong(codes = { PublishCode.MONG_SHIFT })
    @SendCommit(topic = KafkaService.CommitTopic.EVOLUTION_READY)
    @Transactional
    public MongVo evolutionReady(Long mongId) {
        return mongService.toggleEvolutionReady(mongId);
    }

    @SendCommit(topic = KafkaService.CommitTopic.FIRST_EVOLUTION_MONG)
    @Transactional
    public MongVo firstEvolution(MongVo mongVo) {
        MongVo newMongVo;

        List<MongCode> mongCodeList = codeService.getMongCodeByLevel(MongGrade.ZERO.nextGrade.level);
        int randIdx = random.nextInt(mongCodeList.size());
        String mongCode = mongCodeList.get(randIdx).code();

        newMongVo = mongService.toggleFirstEvolution(mongVo.mongId(), mongCode);

        return newMongVo;
    }

    @SendCommit(topic = KafkaService.CommitTopic.EVOLUTION_MONG)
    @Transactional
    public MongVo evolution(MongVo mongVo) {
        MongVo newMongVo;
        // TODO("진화 포인트 환산")
        int evolutionPoint = 0;

        List<MongCode> mongCodeList = codeService.getMongCodeByLevelAndEvolutionPoint(mongVo.grade().nextGrade.level, evolutionPoint);

        // TODO("컬렉션 목록을 조회하여 겹치지 않도록 하는 로직 필요")

        String mongCode = mongCodeList.get(mongCodeList.size() - 1).code();

        newMongVo = mongService.toggleEvolution(mongVo.mongId(), mongCode);

        return newMongVo;
    }

    @SendCommit(topic = KafkaService.CommitTopic.LAST_EVOLUTION_MONG)
    @Transactional
    public MongVo lastEvolution(MongVo mongVo) {
        return mongService.toggleLastEvolution(mongVo.mongId());
    }
}
