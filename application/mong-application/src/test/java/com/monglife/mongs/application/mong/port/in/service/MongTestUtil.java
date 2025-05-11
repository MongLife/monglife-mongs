package com.monglife.mongs.application.mong.port.in.service;

import com.monglife.mongs.domain.enums.MongStateCode;
import com.monglife.mongs.domain.enums.MongStatusCode;
import com.monglife.mongs.domain.model.Food;
import com.monglife.mongs.domain.model.Mong;
import com.monglife.mongs.domain.model.Snack;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MongTestUtil {

    /**
     * 테스트 음식 생성
     * @param price 음식 가격
     * @param isCanBuy 음식 구매 가능 여부
     * @param status 음식 섭취 시, 증가할 지수 수치
     * @return 음식 도메인 객체
     */
    public static Food getFood(Integer price, Boolean isCanBuy, Double status) {
        return Food.builder()
                .foodTypeCode("TEST-FOOD-YPE-CODE")
                .foodTypeName("테스트 음식 코드")
                .price(price)
                .isCanBuy(isCanBuy)
                .weight(status)
                .strength(status)
                .satiety(status)
                .healthy(status)
                .fatigue(status)
                .build();
    }

    /**
     * 테스트 간식 생성
     * @param price 간식 가격
     * @param isCanBuy 간식 구매 가능 여부
     * @param status 간식 섭취 시, 증가할 지수 수치
     * @return 간식 도메인 객체
     */
    public static Snack getSnack(Integer price, Boolean isCanBuy, Double status) {
        return Snack.builder()
                .snackTypeCode("TEST-SNACK-YPE-CODE")
                .snackTypeName("테스트 간식 코드")
                .price(price)
                .isCanBuy(isCanBuy)
                .weight(status)
                .strength(status)
                .satiety(status)
                .healthy(status)
                .fatigue(status)
                .build();
    }

    /**
     * 알 상태의 테스트 몽 생성
     * @param mongId 몽 ID
     * @param accountId 계정 ID
     * @param maxStatus 최대 지수 수치
     * @return 몽 도메인 객체
     */
    public static Mong getEggMong(Long mongId, Long accountId, Double maxStatus) {
        return  Mong.builder()
                .mongId(mongId)
                .accountId(accountId)
                .mongName("TEST-MONG-NAME")
                .mongTypeCode("CH000")
                .mongTypeName("TEST-MONG-TYPE-NAME")
                .statusCode(MongStatusCode.NORMAL)
                .stateCode(MongStateCode.NORMAL)
                .level(0)
                .maxStatus(maxStatus)
                .sleepAt(LocalTime.now())
                .wakeupAt(LocalTime.now())
                .payPoint(0)
                .isSleep(false)
                .strength(maxStatus)
                .satiety(maxStatus)
                .healthy(maxStatus)
                .fatigue(maxStatus)
                .exp(0D)
                .weight(0D)
                .evolutionReward(0D)
                .evolutionPenalty(0D)
                .strokeCount(0)
                .trainingCount(0)
                .poopCount(0)
                .randomDrawTicketCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 1레벨의 테스트 몽 생성
     * @param mongId 몽 ID
     * @param accountId 계정 ID
     * @param maxStatus 최대 지수 수치
     * @return 몽 도메인 객체
     */
    public static Mong getFirstLevelMong(Long mongId, Long accountId, Double maxStatus) {
        return getFirstLevelMong(mongId, accountId, maxStatus, maxStatus, 0);
    }

    /**
     * 1레벨의 테스트 몽 생성
     * @param mongId 몽 ID
     * @param accountId 계정 ID
     * @param maxStatus 최대 지수 수치
     * @param status 초기화 할 지수 수치
     * @param payPoint 페이 포인트
     * @return 몽 도메인 객체
     */
    public static Mong getFirstLevelMong(Long mongId, Long accountId, Double status, Double maxStatus, Integer payPoint) {
        return  Mong.builder()
                .mongId(mongId)
                .accountId(accountId)
                .mongName("TEST-MONG-NAME")
                .mongTypeCode("CH100")
                .mongTypeName("TEST-MONG-TYPE-NAME")
                .statusCode(MongStatusCode.NORMAL)
                .stateCode(MongStateCode.NORMAL)
                .level(1)
                .maxStatus(maxStatus)
                .sleepAt(LocalTime.now())
                .wakeupAt(LocalTime.now())
                .payPoint(payPoint)
                .isSleep(false)
                .strength(status)
                .satiety(status)
                .healthy(status)
                .fatigue(status)
                .exp(0D)
                .weight(status)
                .evolutionReward(0D)
                .evolutionPenalty(0D)
                .strokeCount(0)
                .trainingCount(0)
                .poopCount(0)
                .randomDrawTicketCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
