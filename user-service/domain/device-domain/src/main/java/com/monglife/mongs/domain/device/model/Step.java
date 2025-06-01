package com.monglife.mongs.domain.device.model;

import com.monglife.mongs.domain.device.exception.InvalidTotalWalkingCountException;
import com.monglife.mongs.domain.device.exception.NotEnoughCurrentWalkingCountException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Step {

    private static final int PAY_POINT_PER_STEP = 100;

    private final String deviceId;

    private Integer walkingCount;

    private Integer totalWalkingCount;

    private Integer consumeWalkingCount;

    private LocalDateTime deviceBootedAt;

    @Builder
    public Step(String deviceId, Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedAt) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedAt = deviceBootedAt;
    }

    /**
     * 보유 걸음 수 조회
     * @return 보유 걸음 수
     */
    public Integer getCurrentWalkingCount() {
        return this.walkingCount + (this.totalWalkingCount - this.consumeWalkingCount);
    }

    /**
     * 보유 걸음 수 페이 포인트 환전
     * @param walkingCount 환전 걸음 수
     * @return 환전 페이 포인트
     */
    public Integer exchangeWalkingCountToPayPoint(Integer walkingCount) {

        // 보유 걸음 수가 부족한 경우 예외 발생
        if (this.getCurrentWalkingCount() < walkingCount) {
            throw new NotEnoughCurrentWalkingCountException();
        }

        // 보유 걸음 수 감소
        this.decreaseCurrentWalkingCount(walkingCount);

        // 환전할 페이 포인트 반환
        return walkingCount * PAY_POINT_PER_STEP;
    }

    /**
     * 보유 걸음 수 증가
     * @param walkingCount 증가할 보유 걸음 수
     */
    public void increaseCurrentWalkingCount(Integer walkingCount) {
        this.walkingCount = this.walkingCount + walkingCount;
    }

    /**
     * 보유 걸음 수 감소
     * @param walkingCount 감소할 보유 걸음 수
     */
    public void decreaseCurrentWalkingCount(Integer walkingCount) {
        this.consumeWalkingCount = this.consumeWalkingCount + walkingCount;
    }

    /**
     * 기기 총 걸음 수 동기화
     * @param totalWalkingCount 기기에 기록된 총 걸음 수
     */
    public void syncTotalWalkingCount(Integer totalWalkingCount, LocalDateTime deviceBootedDt) {

        if (this.getDeviceBootedAt().equals(deviceBootedDt)) {
            // 기기 부팅 시간이 변경 되지 않은 경우, 걸음 수 동기화
            this.updateTotalWalkingCount(totalWalkingCount);
        } else {
            // 기기 부팅 시간이 변경 된 경우, 걸음 수 초기화
            this.reset(totalWalkingCount, deviceBootedDt);
        }
    }

    /**
     * 기기 총 걸음 수 수정
     * @param totalWalkingCount 기기에 기록된 총 걸음 수
     */
    private void updateTotalWalkingCount(Integer totalWalkingCount) {

        if (totalWalkingCount < this.totalWalkingCount) {
            throw new InvalidTotalWalkingCountException();
        }

        this.totalWalkingCount = totalWalkingCount;
    }

    /**
     * 걸음 수 초기화
     * @param totalWalkingCount 기기에 기록된 총 걸음 수
     * @param deviceBootedDt 기기에 기록된 부팅 시간
     */
    private void reset(Integer totalWalkingCount, LocalDateTime deviceBootedDt) {
        if (this.deviceBootedAt.isBefore(deviceBootedDt)) {
            this.walkingCount = this.walkingCount + this.totalWalkingCount - this.consumeWalkingCount;
            this.totalWalkingCount = totalWalkingCount;
            this.consumeWalkingCount = 0;
            this.deviceBootedAt = deviceBootedDt;
        }
    }
}
