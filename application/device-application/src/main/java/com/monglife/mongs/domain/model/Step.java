package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.exception.InvalidTotalWalkingCountException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Step {

    private String deviceId;

    private Integer walkingCount;

    private Integer totalWalkingCount;

    private Integer consumeWalkingCount;

    private LocalDateTime deviceBootedDt;

    @Builder
    public Step(String deviceId, Integer walkingCount, Integer totalWalkingCount, Integer consumeWalkingCount, LocalDateTime deviceBootedDt) {
        this.deviceId = deviceId;
        this.walkingCount = walkingCount;
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.deviceBootedDt = deviceBootedDt;
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
     * 보유 걸음 수 조회
     * @return 보유 걸음 수
     */
    public Integer getCurrentWalkingCount() {
        return this.walkingCount + (this.totalWalkingCount - this.consumeWalkingCount);
    }

    /**
     * 기기 총 걸음 수 동기화
     * @param totalWalkingCount 기기에 기록된 총 걸음 수
     */
    public void updateTotalWalkingCount(Integer totalWalkingCount) {

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
    public void reset(Integer totalWalkingCount, LocalDateTime deviceBootedDt) {
        if (this.deviceBootedDt.isBefore(deviceBootedDt)) {
            this.walkingCount = this.walkingCount + this.totalWalkingCount - this.consumeWalkingCount;
            this.totalWalkingCount = totalWalkingCount;
            this.consumeWalkingCount = 0;
            this.deviceBootedDt = deviceBootedDt;
        }
    }
}
