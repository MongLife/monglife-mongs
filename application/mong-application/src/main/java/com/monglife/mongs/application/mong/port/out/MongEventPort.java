package com.monglife.mongs.application.mong.port.out;

public interface MongEventPort {

    /**
     * 몽 생성 이벤트
     * @param accountId 계정 ID
     * @param mongTypeCode 몽 타입 코드
     */
    void createMongEventPort(Long accountId, String mongTypeCode);

    /**
     * 몽 진화 이벤트
     * @param accountId 계정 ID
     * @param mongTypeCode 몽 타입 코드
     */
    void evolutionMongEventPort(Long accountId, String mongTypeCode);

    /**
     * 랜덤 맵 뽑기 이벤트
     * @param accountId 계정 ID
     * @param mapTypeCode 맵 타입 코드
     */
    void randomDrawMapEventPort(Long accountId, String mapTypeCode);
}
