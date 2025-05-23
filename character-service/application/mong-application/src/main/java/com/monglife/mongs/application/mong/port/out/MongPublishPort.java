package com.monglife.mongs.application.mong.port.out;

import com.monglife.mongs.domain.mong.model.Mong;

public interface MongPublishPort {

    /**
     * 몽 정보 비동기 응답
     * @param mong 몽 도메인 객체
     */
    void publishMongPort(Mong mong);

    /**
     * 몽 변동 알림 응답
     * @param accountId 계정 ID
     * @param title 알림 제목
     * @param body 알림 본문
     */
    void publishNotificationPort(Long accountId, String title, String body);
}
