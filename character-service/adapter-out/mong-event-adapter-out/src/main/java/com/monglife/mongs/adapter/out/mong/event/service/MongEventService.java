package com.monglife.mongs.adapter.out.mong.event.service;

import com.monglife.module.common.kafka.service.KafkaService;
import com.monglife.mongs.application.mong.port.out.MongEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongEventService  implements MongEventPort {

    private final KafkaService kafkaService;

    /**
     * 몽 생성 이벤트 발생
     * @param accountId 계정 ID
     * @param mongTypeCode 몽 타입 코드
     */
    @Override
    public void createMongEventPort(Long accountId, String mongTypeCode) {

    }

    /**
     * 몽 진화 이벤트 발생
     * @param accountId 계정 ID
     * @param mongTypeCode 몽 타입 코드
     */
    @Override
    public void evolutionMongEventPort(Long accountId, String mongTypeCode) {

    }

    /**
     * 컬렉션 맵 랜덤 뽑기 이벤트 발생
     * @param accountId 계정 ID
     * @param mapTypeCode 맵 타입 코드
     */
    @Override
    public void randomDrawMapEventPort(Long accountId, String mapTypeCode) {

    }
}
