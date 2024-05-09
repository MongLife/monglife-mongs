package com.mongs.play.app.management.external.service;

import com.mongs.play.app.management.external.vo.EvolutionMongVo;
import com.mongs.play.domain.mong.service.MongService;
import com.mongs.play.module.kafka.event.commit.EvolutionMongEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import com.mongs.play.module.kafka.service.KafkaService.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementExternalService {

    private final MongService mongService;
    private final KafkaService kafkaService;

    public EvolutionMongVo evolutionMong(Long accountId, Long mongId) {

        String mongCode = "CH100";

        // TODO(진화로직)

        kafkaService.sendCommit(KafkaTopic.EVOLUTION_MONG, EvolutionMongEvent.builder()
                        .accountId(accountId)
                        .mongCode(mongCode)
                        .build());

        return EvolutionMongVo.builder()
                .build();
    }
}
