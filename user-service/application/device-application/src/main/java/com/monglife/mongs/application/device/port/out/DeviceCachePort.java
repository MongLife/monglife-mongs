package com.monglife.mongs.application.device.port.out;

public interface DeviceCachePort {

    /**
     * 오늘 누적 환전 걸음 수를 늘리고, 늘린 뒤의 값을 반환한다.
     * @return 오늘 누적 환전 걸음 수. 집계할 수 없으면 0
     */
    int increaseTodayExchangedWalkingCountPort(Long accountId, Integer walkingCount);

    /**
     * 오늘 누적 환전 걸음 수를 되돌린다.
     * 상한을 넘겨 환전을 거절했거나, 환전 이벤트 발행이 실패했을 때 쓴다.
     */
    void decreaseTodayExchangedWalkingCountPort(Long accountId, Integer walkingCount);
}
