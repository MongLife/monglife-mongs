package com.monglife.mongs.domain.device.model;

import com.monglife.mongs.domain.device.exception.ExceedDailyExchangeWalkingCountException;
import com.monglife.mongs.domain.device.exception.InvalidExchangeWalkingCountException;
import lombok.Getter;
import lombok.ToString;

/**
 * 걸음 수 환전
 *
 * 걸음 수 잔액은 기기가 들고 있고 서버는 보관하지 않는다. 서버에 남은 규칙은 두 가지뿐이다.
 * 요청받은 걸음 수를 페이 포인트로 환산하는 환율, 그리고 하루에 환전할 수 있는 상한이다.
 * 그래서 이 객체는 기기 상태를 담지 않고 환전 1건의 결과만 표현한다.
 */
@Getter
@ToString
public class Step {

    private static final double PAY_POINT_PER_STEP = 0.1;

    /**
     * 계정당 하루 환전 상한.
     *
     * 서버가 걸음 수 잔액을 보관하지 않게 되면서 환전 요청의 정당성을 검증할 근거가 사라졌다.
     * 사람이 하루에 걸을 수 있는 범위를 넉넉히 넘는 값으로 잡아, 정상 사용자는 건드리지 않으면서
     * 변조 클라이언트의 무제한 발행만 막는다.
     */
    public static final int DAILY_EXCHANGE_LIMIT_WALKING_COUNT = 30_000;

    private final Integer walkingCount;

    private final Integer payPoint;

    private Step(Integer walkingCount, Integer payPoint) {
        this.walkingCount = walkingCount;
        this.payPoint = payPoint;
    }

    /**
     * 환전 걸음 수를 검증하고 지급할 페이 포인트를 산출한다.
     * @param walkingCount 환전할 걸음 수
     */
    public static Step of(Integer walkingCount) {

        if (walkingCount == null || walkingCount <= 0) {
            throw new InvalidExchangeWalkingCountException();
        }

        return new Step(walkingCount, (int) Math.ceil(walkingCount * PAY_POINT_PER_STEP));
    }

    /**
     * 오늘 누적 환전량이 상한을 넘었는지 검증한다.
     * @param todayExchangedWalkingCount 이번 요청까지 더한 오늘 누적 환전 걸음 수
     */
    public static void validateDailyExchangeLimit(int todayExchangedWalkingCount) {

        if (todayExchangedWalkingCount > DAILY_EXCHANGE_LIMIT_WALKING_COUNT) {
            throw new ExceedDailyExchangeWalkingCountException();
        }
    }
}
