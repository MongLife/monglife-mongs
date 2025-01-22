package com.monglife.mongs.app.activity.battle.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.app.activity.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.FightBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.activity.battle.exception.NotExistsMatchException;
import com.monglife.mongs.app.activity.battle.exception.NotExistsMongException;
import com.monglife.mongs.app.activity.battle.vo.CreateBattleVo;
import com.monglife.mongs.client.manager.service.ManagementService;
import com.monglife.mongs.client.manager.vo.MongVo;
import com.monglife.mongs.domain.match.dto.etc.CreateMatchDto;
import com.monglife.mongs.domain.match.dto.etc.EnterMatchDto;
import com.monglife.mongs.domain.match.dto.etc.ExitMatchDto;
import com.monglife.mongs.domain.match.dto.etc.PickMatchDto;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import com.monglife.mongs.domain.match.service.MatchService;
import com.monglife.mongs.domain.match.service.MatchingService;
import com.monglife.mongs.domain.match.vo.CreateMatchVo;
import com.monglife.mongs.domain.match.vo.FightMatchVo;
import com.monglife.mongs.domain.match.vo.OverMatchVo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final MatchService matchService;

    private final MatchingService matchingService;
    private final ManagementService managementService;

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
     * @param createBattleVoSet 배틀 생성 정보 Dto
     * @return 배틀 생성 완료 Dto
     */
    @Transactional
    public CreateBattleDto createBattle(Set<CreateBattleVo> createBattleVoSet) {

        Map<Long, MongVo> mongVoMap = new HashMap<>();

        // 봇이 아닌 플레이어 몽 정보 조회
        for (CreateBattleVo createBattleVo : createBattleVoSet) {
            // 봇인 경우 패스
            if (createBattleVo.getIsBot()) continue;

            // manager-management 조회 요청 (feign)
            Long mongId = createBattleVo.getMongId();
            Optional<MongVo> optionalMongVo = managementService.getMong(mongId);

            if (optionalMongVo.isPresent()) {
                // 몽 정보가 있는 경우
                MongVo mongVo = optionalMongVo.get();
                mongVoMap.put(mongVo.getMongId(), mongVo);
            } else {
                // 몽 정보가 없는 경우
                createBattleVoSet.stream()
                        // 조회 실패 몽과 봇을 제외한 모든 플레이어 매칭 대기열 복귀
                        .filter(vo -> !vo.getMongId().equals(mongId) && !createBattleVo.getIsBot())
                        .forEach(vo -> matchingService.createWaitMatching(vo.getAccountId(), vo.getDeviceId(), vo.getMongId()));

                throw new NotExistsMongException(mongId);
            }
        }

        Set<CreateMatchVo> createMatchVoSet = createBattleVoSet.stream()
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

                    return CreateMatchVo.builder()
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
        CreateMatchDto createMatchDto = matchService.createMatch(createMatchVoSet);

        return CreateBattleDto.builder()
                .roomId(createMatchDto.getRoomId())
                .battlePlayers(createMatchDto.getMatchPlayers())
                .build();
    }

    /**
     * 매치 입장
     * @param roomId 배틀룸 ID
     * @param playerId 플레이어 ID
     * @return 매치 입장 정보 Dto
     */
    @Transactional
    public FightBattleDto enterBattle(Long roomId, String playerId) {

        EnterMatchDto enterMatchDto = matchService.enterMatch(roomId, playerId);

        Boolean isEnterAll = enterMatchDto.getIsEnterAll();
        FightMatchVo fightMatchVo = enterMatchDto.getFightMatchVo();

        FightBattleDto fightBattleDto = null;

        if (isEnterAll && fightMatchVo != null) {
            fightBattleDto = FightBattleDto.builder()
                    .roomId(roomId)
                    .round(fightMatchVo.getRound())
                    .isLastRound(fightMatchVo.getIsLastRound())
                    .battlePlayers(fightMatchVo.getMatchPlayers())
                    .build();
        }

        return fightBattleDto;
    }

    /**
     * 매치 퇴장
     * @param roomId 배틀룸 ID
     * @param playerId 플레이어 ID
     * @return 매치 종료 정보 Dto
     */
    @Transactional
    public OverBattleDto exitBattle(Long roomId, String playerId) {

        ExitMatchDto exitMatchDto = matchService.exitMatch(roomId, playerId);

        Boolean isExitAll = exitMatchDto.getIsExitAll();
        List<OverMatchVo> overMatchVos = exitMatchDto.getOverMatchVos();

        OverBattleDto overBattleDto = null;

        if (isExitAll && !overMatchVos.isEmpty()) {
            // 남은 플레이어 1명 승리로 처리
            matchService.overMatch(roomId);

            OverMatchVo overMatchVo = overMatchVos.stream().findFirst()
                    .orElseThrow(() -> new NotExistsMatchException(roomId));

            overBattleDto = OverBattleDto.builder()
                    .roomId(roomId)
                    .winPlayerId(overMatchVo.getPlayerId())
                    .winMongTypeCode(overMatchVo.getMongTypeCode())
                    .build();
        }

        return overBattleDto;
    }

    /**
     * 매치 선택
     * @param roomId 배틀룸 ID
     * @param playerId 플레이어 ID
     * @param targetPlayerId 상대 플레이어 ID
     * @param matchRoundCode 매치 선택 코드
     * @return 매치 라운드 종료 정보 Dto
     */
    @Transactional
    public FightBattleDto pickBattle(Long roomId, String playerId, String targetPlayerId, MatchRoundCode matchRoundCode) {

        PickMatchDto pickMatchDto = matchService.pickMatch(roomId, playerId, targetPlayerId, matchRoundCode);

        Boolean isPickAll = pickMatchDto.getIsPickAll();
        FightMatchVo fightMatchVo = pickMatchDto.getFightMatchVo();

        FightBattleDto fightBattleDto = null;

        if (isPickAll && fightMatchVo != null) {
            // 마지막 라운드 인 경우 배틀 종료 처리
            if (fightMatchVo.getIsLastRound()) {
                matchService.overMatch(roomId);
            }

            fightBattleDto = FightBattleDto.builder()
                    .roomId(roomId)
                    .round(fightMatchVo.getRound())
                    .battlePlayers(fightMatchVo.getMatchPlayers())
                    .isLastRound(fightMatchVo.getIsLastRound())
                    .build();
        }

        return fightBattleDto;
    }

    /**
     * 매치 종료 정보 조회
     * @param roomId 배틀룸 ID
     * @return 매치 종료 정보 Dto
     */
    @Transactional
    public OverBattleDto findOverBattle(Long roomId) {

        List<OverMatchVo> overMatchVos = matchService.findOverMatch(roomId);

        OverMatchVo overMatchVo = overMatchVos.stream().findFirst()
                .orElseThrow(() -> new NotExistsMatchException(roomId));

        return OverBattleDto.builder()
                .roomId(roomId)
                .winMongTypeCode(overMatchVo.getPlayerId())
                .winMongTypeCode(overMatchVo.getMongTypeCode())
                .build();
    }
}
