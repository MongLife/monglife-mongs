package com.monglife.mongs.domain.member.model;

import com.monglife.mongs.domain.member.exception.AlreadyMaxSlotCountException;
import com.monglife.mongs.domain.member.exception.NotEnoughStarPointException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Player {

    private static final Integer PAY_POINT_PER_STAR_POINT = 1000;
    private static final Integer MAX_SLOT_COUNT = 3;
    private static final Integer SLOT_PRICE = 10;

    private final Long accountId;

    private Integer slotCount;

    private Integer starPoint;

    @Builder
    public Player(Long accountId, Integer slotCount, Integer starPoint) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
    }

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
     * 스타 포인트 페이 포인트 환전
     * @param starPoint 감소할 스타 포인트
     * @return 환전 페이 포인트
     */
    public Integer exchangeStarPointToPayPoint(Integer starPoint) {

        this.decreaseStarPoint(starPoint);

        return starPoint * PAY_POINT_PER_STAR_POINT;
    }

    /**
     * 스타 포인트 감소
     * @param starPoint 감소할 스타 포인트
     */
    private void decreaseStarPoint(Integer starPoint) {

        if (this.starPoint < starPoint) {
            throw new NotEnoughStarPointException();
        }

        this.starPoint = Math.max(0, this.starPoint - starPoint);
    }

    /**
     * 스타 포인트 증가
     * @param starPoint 증가할 스타 포인트
     */
    public void increaseStarPoint(Integer starPoint) {
        this.starPoint = this.starPoint + starPoint;
    }

}
