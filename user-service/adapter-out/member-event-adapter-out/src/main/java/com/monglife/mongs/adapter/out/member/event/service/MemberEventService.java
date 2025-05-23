package com.monglife.mongs.adapter.out.member.event.service;

import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.adapter.transaction.ExchangeStarPointEventDto;
import com.monglife.mongs.application.member.port.out.MemberEventPort;
import com.monglife.mongs.core.kafka.event.enums.EventTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberEventService implements MemberEventPort {

    private final KafkaService kafkaService;

    /**
     * 스타 포인트 환전 분산 트랜잭션 이벤트 발생
     * @param mongId 몽 ID
     * @param starPoint 환전할 스타 포인트
     * @param payPoint 환전할 페이 포인트
     */
    @Override
    public void exchangeStarPointEventPort(Long accountId, Long mongId, Integer starPoint, Integer payPoint) {

        ExchangeStarPointEventDto exchangeStarPointEventDto = ExchangeStarPointEventDto.builder()
                .accountId(accountId)
                .mongId(mongId)
                .starPoint(starPoint)
                .payPoint(payPoint)
                .build();

        kafkaService.generateEvent(EventTopic.COMMIT_EXCHANGE_STAR_POINT, exchangeStarPointEventDto);
    }
}
