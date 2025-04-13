package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.exception.AlreadyMaxSlotCountException;
import com.monglife.mongs.domain.exception.NotEnoughStarPointException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Player {

    private Long accountId;

    private Integer slotCount;

    private Integer starPoint;

    @Builder
    public Player(Long accountId, Integer slotCount, Integer starPoint) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
    }

    private static final Integer MAX_SLOT_COUNT = 3;
    private static final Integer SLOT_PRICE = 10;

    /**
     * 슬롯 구매
     */
    public void buySlot() {

        if (this.starPoint < SLOT_PRICE) {
            throw new NotEnoughStarPointException();
        }

        if (this.slotCount >= MAX_SLOT_COUNT) {
            throw new AlreadyMaxSlotCountException();
        }

        this.starPoint = this.starPoint - SLOT_PRICE;
        this.slotCount = this.slotCount + 1;
    }

    /**
     * 스타 포인트 증가
     * @param starPoint 증가할 스타 포인트
     */
    public void increaseStarPoint(Integer starPoint) {
        this.starPoint = this.starPoint + starPoint;
    }

    /**
     * 스타 포인트 감소
     * @param starPoint 감소할 스타 포인트
     */
    public void decreaseStarPoint(Integer starPoint) {

        if (this.starPoint < starPoint) {
            throw new NotEnoughStarPointException();
        }

        this.starPoint = this.starPoint - starPoint;
    }
}
