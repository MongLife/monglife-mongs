package com.monglife.mongs.application.mong.port.in.service;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.model.Food;
import com.monglife.mongs.domain.mong.model.Mong;
import com.monglife.mongs.domain.mong.model.MongType;
import com.monglife.mongs.domain.mong.model.Snack;

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
     * @return 몽 도메인 객체
     */
    public static Mong getFirstLevelMong(Long mongId, Long accountId, Double maxStatus, MongStateCode mongStateCode) {
        return getFirstLevelMong(mongId, accountId, maxStatus, maxStatus, 0, mongStateCode, MongStatusCode.NORMAL, false, 0);
    }

    /**
     * 1레벨의 테스트 몽 생성
     * @return 몽 도메인 객체
     */
    public static Mong getFirstLevelMong(Long mongId, Long accountId, Double maxStatus, MongStatusCode mongStatusCode) {
        return getFirstLevelMong(mongId, accountId, maxStatus, maxStatus, 0, MongStateCode.NORMAL, mongStatusCode, false, 0);
    }

    /**
     * 1레벨의 테스트 몽 생성
     * @return 몽 도메인 객체
     */
    public static Mong getFirstLevelMong(Long mongId, Long accountId, Double maxStatus, Boolean isSleep) {
        return getFirstLevelMong(mongId, accountId, maxStatus, maxStatus, 0, MongStateCode.NORMAL, MongStatusCode.NORMAL, isSleep, 0);
    }

    /**
     * 1레벨의 테스트 몽 생성
     * @return 몽 도메인 객체
     */
    public static Mong getFirstLevelMong(Long mongId, Long accountId, Double maxStatus, Integer poopCount) {
        return getFirstLevelMong(mongId, accountId, maxStatus, maxStatus, 0, MongStateCode.NORMAL, MongStatusCode.NORMAL, false, poopCount);
    }

    /**
     * 1레벨의 테스트 몽 생성
     * @return 몽 도메인 객체
     */
    public static Mong getFirstLevelMong(Long mongId, Long accountId, Double status, Double maxStatus) {
        return getFirstLevelMong(mongId, accountId, status, maxStatus, 0, MongStateCode.NORMAL, MongStatusCode.NORMAL, false, 0);
    }

    /**
     * 1레벨의 테스트 몽 생성
     * @return 몽 도메인 객체
     */
    public static Mong getFirstLevelMong(Long mongId, Long accountId, Double status, Double maxStatus, Integer payPoint) {
        return getFirstLevelMong(mongId, accountId, status, maxStatus, payPoint, MongStateCode.NORMAL, MongStatusCode.NORMAL, false, 0);
    }

    /**
     * 1레벨의 테스트 몽 생성
     * @return 몽 도메인 객체
     */
    public static Mong getFirstLevelMong(Long mongId, Long accountId, Double status, Double maxStatus, Integer payPoint, MongStateCode mongStateCode, MongStatusCode mongStatusCode, Boolean isSleep, Integer poopCount) {
        return  Mong.builder()
                .mongId(mongId)
                .accountId(accountId)
                .mongName("TEST-MONG-NAME")
                .mongTypeCode("CH100")
                .mongTypeName("TEST-MONG-TYPE-NAME")
                .stateCode(mongStateCode)
                .statusCode(mongStatusCode)
                .level(1)
                .maxStatus(maxStatus)
                .sleepAt(LocalTime.now())
                .wakeupAt(LocalTime.now())
                .payPoint(payPoint)
                .isSleep(isSleep)
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
                .poopCount(poopCount)
                .randomDrawTicketCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 알 상태의 몽 타입 생성
     * @param maxStatus 최대 지수 수치
     * @return 몽 타입 도메인 객체
     */
    public static MongType getEggMongType(Double maxStatus) {
        return MongType.builder()
                .mongTypeId(1L)
                .mongTypeCode("CH000")
                .mongTypeName("테스트 몽 타입 1")
                .level(0)
                .evolutionScore(0D)
                .maxStatus(maxStatus)
                .build();
    }

    /**
     * 1레벨의 몽 타입 생성
     * @param maxStatus 최대 지수 수치
     * @return 몽 타입 도메인 객체
     */
    public static MongType getFirstLevelMongType(Double maxStatus) {
        return MongType.builder()
                .mongTypeId(1L)
                .mongTypeCode("CH100")
                .mongTypeName("테스트 몽 타입 1")
                .level(1)
                .evolutionScore(0D)
                .maxStatus(maxStatus)
                .build();
    }

    /**
     * 2레벨의 몽 타입 생성
     * @param maxStatus 최대 지수 수치
     * @return 몽 타입 도메인 객체
     */
    public static MongType getSecondLevelMongType(Double maxStatus) {
        return MongType.builder()
                .mongTypeId(1L)
                .mongTypeCode("CH200")
                .mongTypeName("테스트 몽 타입 1")
                .level(2)
                .evolutionScore(0D)
                .maxStatus(maxStatus)
                .build();
    }
}
