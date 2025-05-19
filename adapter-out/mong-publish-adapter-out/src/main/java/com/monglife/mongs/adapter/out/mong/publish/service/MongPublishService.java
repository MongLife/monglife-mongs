package com.monglife.mongs.adapter.out.mong.publish.service;

import com.monglife.mongs.application.mong.port.out.MongPublishPort;
import com.monglife.mongs.domain.mong.model.Mong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongPublishService implements MongPublishPort {

    /**
     * 몽 정보 비동기 응답
     * @param mong 몽 도메인 객체
     */
    @Override
    public void publishMongPort(Mong mong) {

    }
}
