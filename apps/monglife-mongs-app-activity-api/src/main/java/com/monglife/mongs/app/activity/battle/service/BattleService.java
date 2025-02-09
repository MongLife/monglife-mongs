package com.monglife.mongs.app.activity.battle.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.app.activity.battle.config.BattleProperties;
import com.monglife.mongs.app.activity.battle.dto.etc.BattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.client.manager.service.ManagementService;
import com.monglife.mongs.client.manager.vo.MongVo;
import com.monglife.mongs.app.activity.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import com.monglife.mongs.domain.match.service.MatchService;
import com.monglife.mongs.domain.match.service.MatchingService;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import com.monglife.mongs.domain.match.vo.MatchVo;
import com.monglife.mongs.domain.match.vo.MatchingPlayerVo;
import com.monglife.mongs.domain.match.vo.MatchingVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleService {

    private final MatchService matchService;

    private final MatchingService matchingService;

    private final ManagementService managementService;

    private final BattleProperties battleProperties;

    /**
     * 배틀 정보 조회
     * @param roomId 배틀룸 ID
     * @return 배틀룸 정보 Dto
     */
    @Transactional
    public BattleDto getBattle(Long roomId) {

        MatchVo matchVo = matchService.getMatch(roomId);

        return BattleDto.of(matchVo);
    }

    /**
     * 매치 종료 정보 조회
     * @param roomId 배틀룸 ID
     * @return 매치 종료 정보 Dto
     */
    @Transactional
    public OverBattleDto getOverBattle(Long roomId) {

        MatchPlayerVo matchPlayerVo = matchService.getWinMatchPlayer(roomId);

        return OverBattleDto.builder()
                .roomId(roomId)
                .winPlayerId(matchPlayerVo.getPlayerId())
                .winMongTypeCode(matchPlayerVo.getMongTypeCode())
                .build();
    }

    /**
     * 배틀 매치 보상 페이 포인트 조회
     * @return 승자 페이 포인트
     */
    @Transactional
    public Integer getBattleRewardPayPoint() {
        return battleProperties.rewardPayPoint;
    }

    /**
     * 배틀 매치 배팅 페이 포인트 조회
     * @return 배팅 페이 포인트
     */
    @Transactional
    public Integer getBattleBettingPayPoint() {
        return battleProperties.bettingPayPoint;
    }

    /**
     * 매칭 대기열 등록
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param deviceId 기기 ID
     */
    @Transactional
    public void createWaitMatching(Long accountId, Long mongId, String deviceId) {
        matchingService.createWaitMatching(accountId, deviceId, mongId);
    }

    /**
     * 매칭 대기열 삭제
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param deviceId 기기 ID
     */
    @Transactional
    public void deleteWaitMatching(Long accountId, Long mongId, String deviceId) {
        matchingService.deleteWaitMatching(accountId, deviceId, mongId);
    }

    /**
     * 배틀 생성
     * @param matchingVoSet 배틀 생성 정보 Dto
     */
    @Transactional
    public CreateBattleDto createBattle(Set<MatchingVo> matchingVoSet) {

        Map<Long, MongVo> mongVoMap = new HashMap<>();

        // 봇이 아닌 플레이어 몽 정보 조회
        for (MatchingVo matchingVo : matchingVoSet) {
            // 봇인 경우 패스
            if (matchingVo.getIsBot()) continue;

            // manager-management 조회 요청 (feign)
            Long mongId = matchingVo.getMongId();

            Optional<MongVo> optionalMongVo = managementService.getMong(mongId);

            if (optionalMongVo.isPresent()) {
                // 몽 정보가 있는 경우
                MongVo mongVo = optionalMongVo.get();
                mongVoMap.put(mongVo.getMongId(), mongVo);

                // 배팅 포인트 소비
                managementService.consumePayPoint(mongVo.getMongId(), battleProperties.bettingPayPoint);

            } else {
                // 몽 정보가 없는 경우
                matchingVoSet.stream()
                        // 조회 실패 몽과 봇을 제외한 모든 플레이어 매칭 대기열 복귀
                        .filter(vo -> !vo.getMongId().equals(mongId) && !matchingVo.getIsBot())
                        .forEach(vo -> matchingService.createWaitMatching(vo.getAccountId(), vo.getDeviceId(), vo.getMongId()));

                return null;
            }
        }

        Set<MatchingPlayerVo> matchingPlayerVoSet = matchingVoSet.stream()
                .map(createBattleVo -> {

                    String playerId = CommonUtil.randomId();
                    double weight = 0D;
                    double strength = 0D;
                    double fatigue = 0D;
                    String mongTypeCode = "";

                    // 봇이 아닌 경우 조회한 몽 정보로 갱신
                    if (!createBattleVo.getIsBot()) {
                        MongVo mongVo = mongVoMap.get(createBattleVo.getMongId());

                        weight = mongVo.getWeight();
                        strength = mongVo.getStrength();
                        fatigue = mongVo.getFatigue();
                        mongTypeCode = mongVo.getMongTypeCode();
                    }

                    return MatchingPlayerVo.builder()
                            .playerId(playerId)
                            .deviceId(createBattleVo.getDeviceId())
                            .accountId(createBattleVo.getAccountId())
                            .mongId(createBattleVo.getMongId())
                            .mongTypeCode(mongTypeCode)
                            .weight(weight)
                            .strength(strength)
                            .fatigue(fatigue)
                            .isBot(createBattleVo.getIsBot())
                            .build();

                })
                .collect(Collectors.toSet());

        // 새로운 매치 생성
        MatchVo matchVo = matchService.createMatch(matchingPlayerVoSet);

        return CreateBattleDto.of(matchVo);
    }

    /**
     * 매치 입장
     * @param roomId 배틀룸 ID
     * @param playerId 플레이어 ID
     */
    @Transactional
    public void enterBattle(Long roomId, String playerId) {
        matchService.enterMatch(roomId, playerId);
    }

    /**
     * 매치 퇴장
     * @param roomId 배틀룸 ID
     * @param playerId 플레이어 ID
     */
    @Transactional
    public void exitBattle(Long roomId, String playerId) {
        matchService.exitMatch(roomId, playerId);
    }

    /**
     * 매치 선택
     * @param roomId 배틀룸 ID
     * @param playerId 플레이어 ID
     * @param targetPlayerId 상대 플레이어 ID
     * @param matchRoundCode 매치 선택 코드
     */
    @Transactional
    public void pickBattle(Long roomId, String playerId, String targetPlayerId, MatchRoundCode matchRoundCode) {
        matchService.pickMatch(roomId, playerId, targetPlayerId, matchRoundCode);
    }
}
