package com.monglife.mongs.domain.mong.model;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.exception.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@ToString
public class Mong {

    // 최대 레벨
    private static final int MAX_LEVEL = 3;
    // 최대 배변 수
    private static final int MAX_POOP_COUNT = 4;
    // 쓰다 듬기 대기 시간
    private static final long STROKE_EXPIRED_SECONDS = 300L;
    // 랜덤 뽑기 시 소비 페이 포인트
    private static final int RANDOM_DRAW_PAY_POINT = 100;

    private final Long mongId;

    private final Long accountId;

    private final String mongName;

    private String mongTypeCode;

    private String mongTypeName;

    private MongStatusCode statusCode;

    private MongStateCode stateCode;

    private Integer level;

    private Double maxStatus;

    private final LocalTime sleepAt;

    private final LocalTime wakeupAt;

    private Integer payPoint;

    private Boolean isSleep;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double fatigue;

    private Double exp;

    private Double weight;

    private Double evolutionReward;

    private Double evolutionPenalty;

    private Integer strokeCount;

    private Integer trainingCount;

    private Integer poopCount;

    private Integer randomDrawTicketCount;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    @Builder
    public Mong(Long mongId, Long accountId, String mongName, String mongTypeCode, String mongTypeName, MongStatusCode statusCode, MongStateCode stateCode, Integer level, Double maxStatus, LocalTime sleepAt, LocalTime wakeupAt, Integer payPoint, Boolean isSleep, Double strength, Double satiety, Double healthy, Double fatigue, Double exp, Double weight, Double evolutionReward, Double evolutionPenalty, Integer strokeCount, Integer trainingCount, Integer poopCount, Integer randomDrawTicketCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.accountId = accountId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.statusCode = statusCode;
        this.stateCode = stateCode;
        this.level = level;
        this.maxStatus = maxStatus;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
        this.payPoint = payPoint;
        this.isSleep = isSleep;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.exp = exp;
        this.weight = weight;
        this.evolutionReward = evolutionReward;
        this.evolutionPenalty = evolutionPenalty;
        this.strokeCount = strokeCount;
        this.trainingCount = trainingCount;
        this.poopCount = poopCount;
        this.randomDrawTicketCount = randomDrawTicketCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 쓰다 듬기 대기 시간 조회
     * @return 쓰다 듬기 대기 시간
     */
    public static Long getStrokeExpirationSeconds() {
        return STROKE_EXPIRED_SECONDS;
    }

    /**
     * 최대 배변 수 조회
     */
    public static Integer getMaxPoopCount() {
        return MAX_POOP_COUNT;
    }

    /**
     * 몽 권한 확인
     * @param accountId 계정 ID
     * @return 몽 도메인 객체
     */
    public Mong verify(Long accountId) {

        if (!this.accountId.equals(accountId)) {
            throw new ForbiddenMongException();
        }

        return this;
    }

    /**
     * 몽 사망
     */
    public void dead() {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            MongStateCode.EVOLUTION_READY.equals(this.stateCode) ||
            MongStateCode.GRADUATE_READY.equals(this.stateCode)
        ) throw new InvalidMongStateException();

        this.stateCode = MongStateCode.DEAD;
    }

    /**
     * 쓰다 듬기
     */
    public void stroke() {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            MongStateCode.GRADUATE_READY.equals(this.stateCode) ||
            this.level == 0 ||
            Boolean.TRUE.equals(this.isSleep)
        ) throw new InvalidMongStateException();

        this.exp = this.exp + 5D;
        this.strokeCount = this.strokeCount + 1;

        // 몽 상태 코드 동기화
        this.syncMongStateCode();
    }

    /**
     * 수면
     */
    public void sleep() {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            MongStateCode.GRADUATE_READY.equals(this.stateCode) ||
            this.level == 0 ||
            Boolean.TRUE.equals(this.isSleep)
        ) throw new InvalidMongStateException();

        this.isSleep = Boolean.TRUE;
    }

    /**
     * 기상
     */
    public void wakeup() {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            MongStateCode.GRADUATE_READY.equals(this.stateCode) ||
            this.level == 0 ||
            Boolean.FALSE.equals(this.isSleep)
        ) throw new InvalidMongStateException();

        this.isSleep = Boolean.FALSE;
    }

    /**
     * 배변 처리
     */
    public void poopClean() {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            MongStateCode.GRADUATE_READY.equals(this.stateCode) ||
            this.level == 0 ||
            Boolean.TRUE.equals(this.isSleep)
        ) throw new InvalidMongStateException();

        this.exp = this.poopCount * 2D;
        this.poopCount = 0;

        // 몽 상태 코드 동기화
        this.syncMongStateCode();
    }

    /**
     * 진화 점수 조회
     * @return 진화 점수
     */
    public Double getEvolutionScore() {

        double evolutionScore = 50D;

        evolutionScore += this.evolutionReward;
        evolutionScore -= this.evolutionPenalty;
        evolutionScore += this.strokeCount * 2D;
        evolutionScore += this.trainingCount * 5D;

        return Math.max(evolutionScore, 0);
    }

    /**
     * 진화
     * @param mongTypes 진화 가능한 몽 타입 목록
     */
    public void evolution(List<MongType> mongTypes) {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            MongStateCode.GRADUATE_READY.equals(this.stateCode) ||
            !MongStateCode.EVOLUTION_READY.equals(this.stateCode)
        ) throw new InvalidMongStateException();

        List<MongType> sortedMongTypes = mongTypes.stream()
                .sorted((o1, o2) -> o2.getEvolutionScore().compareTo(o1.getEvolutionScore()))
                .toList();

        // 진화 가능한 몽 타입이 없는 경우 예외
        if (sortedMongTypes.isEmpty()) {
            throw new InvalidEvolutionException();
        }

        // 지수 수치 -> 지수 비율 퍼센트 변환
        double strengthRatio = this.strength / this.maxStatus * 100;
        double satietyRatio = this.satiety / this.maxStatus * 100;
        double healthyRatio = this.healthy / this.maxStatus * 100;
        double fatigueRatio = this.fatigue / this.maxStatus * 100;

        // 진화 점수 계산
        double evolutionScore = this.getEvolutionScore();
        // 다음 몽 타입 지정
        MongType mongType = sortedMongTypes.get(0);

        // 진화 리워드 점수 갱신
        this.evolutionReward = Math.max(0, Math.min(evolutionScore - 100D, 25D));
        this.evolutionPenalty = 0D;
        this.mongTypeCode = mongType.getMongTypeCode();
        this.mongTypeName = mongType.getMongTypeName();
        this.level = mongType.getLevel();
        this.maxStatus = mongType.getMaxStatus();

        this.strength = mongType.getMaxStatus() * strengthRatio / 100;
        this.satiety = mongType.getMaxStatus() * satietyRatio / 100;
        this.healthy = mongType.getMaxStatus() * healthyRatio / 100;
        this.fatigue = mongType.getMaxStatus() * fatigueRatio / 100;
        this.exp = 0D;

        this.stateCode = MongStateCode.NORMAL;
    }

    /**
     * 몽 졸업
     */
    public void graduate() {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            !MongStateCode.GRADUATE_READY.equals(this.stateCode) ||
            this.level == 0
        ) throw new InvalidMongStateException();

        this.stateCode = MongStateCode.GRADUATE;
        this.statusCode = MongStatusCode.NORMAL;
    }

    /**
     * 몽 페이 포인트 증가
     * @param payPoint 증가할 페이 포인트
     */
    public void increasePayPoint(Integer payPoint) {
        this.payPoint = this.payPoint + payPoint;
    }

    /**
     * 몽 지수 1 Cycle 증가
     */
    public void cycleIncreaseStatus() {

        double addHealthy = this.maxStatus * 0.2;
        double addFatigue = this.maxStatus * 0.6;

        this.healthy = Math.min(this.healthy + addHealthy, this.maxStatus);
        this.fatigue = Math.min(this.fatigue + addFatigue, this.maxStatus);

        // 몽 지수 코드 동기화
        this.syncMongStatusCode();
    }

    /**
     * 몽 지수 1 Cycle 감소
     */
    public void cycleDecreaseStatus() {

        double subWeight   = 1.3;
        double subStrength = 0.7 * this.maxStatus;
        double subSatiety  = 0.5 * this.maxStatus;
        double subHealthy  = 0.5 * this.maxStatus;
        double subFatigue  = 0.3 * this.maxStatus;

        this.weight   = Math.max(this.weight   - subWeight, 0);
        this.strength = Math.max(this.strength - subStrength, 0);
        this.satiety  = Math.max(this.satiety  - subSatiety, 0);
        this.healthy  = Math.max(this.healthy  - subHealthy, 0);
        this.fatigue  = Math.max(this.fatigue  - subFatigue, 0);

        // 몽 지수 코드 동기화
        this.syncMongStatusCode();
    }

    /**
     * 몽 배변 수 1 Cycle 증가
     */
    public void cycleIncreasePoopCount() {

        int addPoopCount = 1;

        // 최대 배변 수를 초과한 경우 진화 패널티 증가
        if (this.poopCount + addPoopCount >= MAX_POOP_COUNT) {
            this.evolutionPenalty = this.evolutionPenalty + 1;
        }

        this.poopCount = Math.min(this.poopCount + addPoopCount, MAX_POOP_COUNT);
    }

    /**
     * 훈련
     * @param trainingType 훈련 타입 도메인 객체
     */
    public void training(TrainingType trainingType) {

        this.exp           = this.exp + trainingType.getExp();
        this.strength      = this.strength + trainingType.getStrength();
        this.satiety       = this.satiety + trainingType.getSatiety();
        this.fatigue       = this.fatigue + trainingType.getFatigue();
        this.weight        = this.weight + trainingType.getWeight();
        this.trainingCount = this.trainingCount + 1;

        // 몽 상태 코드 동기화
        this.syncMongStateCode();
        // 몽 지수 코드 동기화
        this.syncMongStatusCode();
    }

    /**
     * 훈련 목표치 달성 완료
     * @param trainingType 훈련 타입 도메인 객체
     */
    public void trainingWithReward(TrainingType trainingType) {
        // 훈련 완료
        this.training(trainingType);
        // 스코어 달성 시 페이 포인트 증가
        this.payPoint = this.payPoint + trainingType.getPayPoint();
    }

    /**
     * 음식 구매 후 섭취
     * @param food 음식 도메인 객체
     */
    public void feedWithBuy(Food food) {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            this.level == 0 ||
            Boolean.TRUE.equals(this.isSleep)
        ) throw new InvalidMongStateException();

        // 구매할 수 없는 경우 예외 발생
        if (Boolean.FALSE.equals(food.getIsCanBuy())) {
            throw new InvalidFeedFoodException();
        }

        // 충분한 페이 포인트 없는 경우 예외 발생
        if (this.payPoint < food.getPrice()) {
            throw new NotEnoughPayPointException();
        }

        this.payPoint = this.payPoint - food.getPrice();
        this.feed(food);
    }

    /**
     * 간식 구매 후 섭취
     * @param snack 간식 도메인 객체
     */
    public void feedWithBuy(Snack snack) {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            this.level == 0 ||
            Boolean.TRUE.equals(this.isSleep)
        ) throw new InvalidMongStateException();

        // 구매할 수 없는 경우 예외 발생
        if (Boolean.FALSE.equals(snack.getIsCanBuy())) {
            throw new InvalidFeedSnackException();
        }

        // 페이 포인트가 충분하지 않은 경우 예외 발생
        if (this.payPoint < snack.getPrice()) {
            throw new NotEnoughPayPointException();
        }

        this.payPoint = this.payPoint - snack.getPrice();
        this.feed(snack);
    }

    /**
     * 음식 섭취
     * @param food 음식 도메인 객체
     */
    public void feed(Food food) {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            this.level == 0 ||
            Boolean.TRUE.equals(this.isSleep)
        ) throw new InvalidMongStateException();

        this.strength = Math.min(this.strength + food.getStrength(), this.maxStatus);
        this.satiety  = Math.min(this.satiety  + food.getSatiety(), this.maxStatus);
        this.healthy  = Math.min(this.healthy  + food.getHealthy(), this.maxStatus);
        this.fatigue  = Math.min(this.fatigue  + food.getFatigue(), this.maxStatus);
        this.weight   = Math.min(this.weight   + food.getWeight(), this.maxStatus);

        // 몽 지수 코드 동기화
        this.syncMongStatusCode();
    }

    /**
     * 간식 섭취
     * @param snack 간식 도메인 객체
     */
    public void feed(Snack snack) {

        if (MongStateCode.DEAD.equals(this.stateCode) ||
            this.level == 0 ||
            Boolean.TRUE.equals(this.isSleep)
        ) throw new InvalidMongStateException();

        this.strength = Math.min(this.strength + snack.getStrength(), this.maxStatus);
        this.satiety  = Math.min(this.satiety  + snack.getSatiety(), this.maxStatus);
        this.healthy  = Math.min(this.healthy  + snack.getHealthy(), this.maxStatus);
        this.fatigue  = Math.min(this.fatigue  + snack.getFatigue(), this.maxStatus);
        this.weight   = Math.min(this.weight   + snack.getWeight(), this.maxStatus);

        // 몽 지수 코드 동기화
        this.syncMongStatusCode();
    }

    /**
     * 랜덤 뽑기 횟수 감소
     */
    public void decreaseRandomDrawTicketCount() {
        // 랜덤 뽑기 가능 횟수가 없는 경우
        if (this.randomDrawTicketCount == 0) {
            if (this.payPoint < RANDOM_DRAW_PAY_POINT) {
                throw new NotEnoughPayPointException();
            }
            // 뽑기 횟수 페이 포인트 구매
            this.payPoint = this.payPoint - RANDOM_DRAW_PAY_POINT;
            this.randomDrawTicketCount = this.randomDrawTicketCount + 1;
        }

        this.randomDrawTicketCount = this.randomDrawTicketCount - 1;
    }

    /**
     * 매치 배팅
     * @param payPoint 배팅 페이 포인트
     */
    public void matchBetting(Integer payPoint) {
        if (this.payPoint < payPoint) {
            throw new NotEnoughPayPointException();
        }

        // 페이 포인트 감소
        this.payPoint = this.payPoint - payPoint;
    }

    /**
     * 매치 보상
     * @param payPoint 보상 페이 포인트
     * @param exp 보상 경험치
     */
    public void matchReward(Integer payPoint, Double exp) {
        // 페이 포인트 증가
        this.payPoint = this.payPoint + payPoint;
        this.exp = Math.min(this.exp + exp, this.maxStatus);

        // 몽 상태 코드 동기화
        this.syncMongStateCode();
    }

    /**
     * 몽 상태 코드 동기화
     */
    private void syncMongStateCode() {
        // 경험치 기준 상태 검증 및 변경
        if (this.exp >= this.maxStatus) {
            this.stateCode = this.level == MAX_LEVEL ? MongStateCode.GRADUATE_READY : MongStateCode.EVOLUTION_READY;
        }
    }

    /**
     * 몽 지수 코드 동기화
     */
    private void syncMongStatusCode() {

        double satietyRatio = this.satiety / this.maxStatus * 100;
        double healthyRatio = this.healthy / this.maxStatus * 100;
        double fatigueRatio = this.fatigue / this.maxStatus * 100;

        if (healthyRatio < 10) {
            this.statusCode = MongStatusCode.SICK;
        } else if (fatigueRatio < 10) {
            this.statusCode = MongStatusCode.SOMNOLENCE;
        } else if (satietyRatio < 10) {
            this.statusCode = MongStatusCode.HUNGRY;
        } else {
            this.statusCode = MongStatusCode.NORMAL;
        }
    }
}
