package com.monglife.mongs.application.mong.port.out;

public interface MongEventPort {

    /**
     * 몽 생성 이벤트
     * @param accountId 계정 ID
     * @param mongCode 몽 타입 코드
     */
    void createMongEventPort(Long accountId, String mongCode);

    /**
     * 몽 진화 이벤트
     * @param accountId 계정 ID
     * @param mongCode 몽 타입 코드
     */
    void evolutionMongEventPort(Long accountId, String mongCode);

    /**
     * 랜덤 맵 뽑기 이벤트
     * @param accountId 계정 ID
     * @param mapCode 맵 타입 코드
     */
    void randomDrawMapEventPort(Long accountId, String mapCode);
}
