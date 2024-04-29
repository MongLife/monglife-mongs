package com.mongs.management.domain.feed.service;

import com.mongs.core.entity.FoodCode;
import com.mongs.core.enums.management.MongExp;
import com.mongs.management.domain.feed.entity.FeedHistory;
import com.mongs.management.domain.feed.event.vo.FeedMongEvent;
import com.mongs.management.domain.feed.repository.FeedHistoryRepository;
import com.mongs.management.domain.feed.service.vo.FeedMongVo;
import com.mongs.management.domain.feed.service.vo.FindFeedHistoryVo;
import com.mongs.management.global.entity.Mong;
import com.mongs.management.domain.feed.repository.FoodCodeRepository;
import com.mongs.management.global.event.vo.EvolutionCheckEvent;
import com.mongs.management.global.event.vo.StateCheckEvent;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import com.mongs.management.global.moduleService.MongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FoodCodeRepository foodCodeRepository;
    private final FeedHistoryRepository feedHistoryRepository;

    private final MongService mongService;

    private final ApplicationEventPublisher publisher;

    /**
     * 음식을 마지막으로 먹은 시각을 포함한 데이터 목록을 반환한다.
     *
     * @param mongId 몽 Id
     * @param buildVersion 앱 빌드 버전
     * @return {@link List<FindFeedHistoryVo>}
     */
    @Override
    @Transactional
    public List<FindFeedHistoryVo> findFeedHistory(Long mongId, String buildVersion) {
        Map<String, FeedHistory> feedHistoryMap = feedHistoryRepository.findByMongIdOrderByBuyAt(mongId).stream()
                .collect(Collectors.toMap(FeedHistory::getCode, feedHistory -> feedHistory));

        /* 음식 코드 리스트 조회 */
        List<FoodCode> foodCodeList = foodCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
        /* 살 수 있는 상태로 다 초기화 */
        List<FindFeedHistoryVo> findFeedHistoryVoList = FindFeedHistoryVo.toList(foodCodeList);

        return findFeedHistoryVoList.stream()
                .map(findFeedHistoryVo -> {
                    if (feedHistoryMap.containsKey(findFeedHistoryVo.code())) {
                        /* 음식 구매 이력 확인 후 존재하는 경우 */
                        return findFeedHistoryVo.toBuilder()
                                .lastBuyAt(feedHistoryMap.get(findFeedHistoryVo.code()).getBuyAt())
                                .build();
                    } else {
                        /* 음식 구매 이력 확인 후 없는 경우에는 그대로 반환 */
                        return findFeedHistoryVo;
                    }
                }).toList();
    }

    /**
     * 계정 Id 와 몽 Id, 음식 코드로 몽 식사를 진행한다.
     * 음식에 해당하는 증가 값 {@link FoodCode} 에 따라 지수가 증가한다.
     * {@link MongExp#EAT_THE_FOOD} 의 exp 만큼 몽의 Exp 가 증가한다.
     * <p>
     * 진화 가능 여부를 체크한다.
     * 상태 변화 여부를 체크한다.
     * <p>
     * 증가 요소 : weight, strength, satiety, healthy, sleep, exp
     * @param accountId 계정 Id
     * @param mongId 몽 Id
     * @param foodCode 음식 코드
     * @return {@link FeedMongVo}
     */
    @Override
    @Transactional
    public FeedMongVo feedMong(Long accountId, Long mongId, String foodCode) {
        Mong mong = mongService.getMong(mongId, accountId);

        /* 음식 코드가 없는 경우 롤백 */
        FoodCode code = foodCodeRepository.findByCode(foodCode)
                .orElseThrow(() -> new ManagementException(ManagementErrorCode.NOT_FOUND_FOOD_CODE));

        double newWeight = Math.min(mong.getWeight() + code.addWeightValue(), mong.getGrade().getMaxStatus());
        double newStrength = Math.min(mong.getStrength() + code.addStrengthValue(), mong.getGrade().getMaxStatus());
        double newSatiety = Math.min(mong.getSatiety() + code.addSatietyValue(), mong.getGrade().getMaxStatus());
        double newHealthy = Math.min(mong.getHealthy() + code.addHealthyValue(), mong.getGrade().getMaxStatus());
        double newSleep = Math.min(mong.getSleep() + code.addSleepValue(), mong.getGrade().getMaxStatus());

        int newExp = Math.min(mong.getExp() + MongExp.EAT_THE_FOOD.getExp(), mong.getGrade().getNextGrade().getEvolutionExp());

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .weight(newWeight)
                .strength(newStrength)
                .satiety(newSatiety)
                .healthy(newHealthy)
                .sleep(newSleep)
                .exp(newExp)
                .build());

        log.info(LocalDateTime.now().toString());

        /* 식사 기록 저장 */
        feedHistoryRepository.save(FeedHistory.builder()
                .mongId(saveMong.getId())
                .code(code.code())
                .price(code.price())
                .build());

        /* 진화 가능 여부 확인 */
        publisher.publishEvent(new EvolutionCheckEvent(saveMong));
        /* 상태 변화 여부 확인 */
        publisher.publishEvent(new StateCheckEvent(saveMong));

        FeedMongVo feedMongVo = FeedMongVo.of(saveMong);
        publisher.publishEvent(new FeedMongEvent(feedMongVo));
        return feedMongVo;
    }

}
