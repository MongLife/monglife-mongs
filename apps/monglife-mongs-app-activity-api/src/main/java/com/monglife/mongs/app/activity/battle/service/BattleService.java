package com.monglife.mongs.app.activity.battle.service;

import com.monglife.mongs.app.activity.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.FightBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.activity.battle.vo.CreateBattleVo;
import com.monglife.mongs.app.activity.battle.exception.NotExistsMatchException;
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
import com.monglife.mongs.domain.mong.annotation.MongAccountCheck;
import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.domain.mong.service.MongService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final MongService mongService;

    private final MatchService matchService;

    private final MatchingService matchingService;


    @MongAccountCheck
    @Transactional
    public void createWaitMatching(Long accountId, Long mongId, String deviceId) {
        matchingService.createWaitMatching(accountId, deviceId, mongId);
    }

    @MongAccountCheck
    @Transactional
    public void deleteWaitMatching(Long accountId, Long mongId, String deviceId) {
        matchingService.deleteWaitMatching(accountId, deviceId, mongId);
    }

    @Transactional
    public CreateBattleDto createBattle(Set<CreateBattleVo> createBattleVoSet) {

        Set<CreateMatchVo> createMatchVoSet = createBattleVoSet.stream()
                .map(createBattleVo -> {

                    String playerId = UUID.randomUUID().toString().replace("-", "");
                    double weight = 0D;
                    double strength = 0D;
                    double fatigue = 0D;
                    String mongTypeCode = "";

                    if (!createBattleVo.getIsBot()) {
                        GetMongDto getMongDto = mongService.getMong(createBattleVo.getMongId());
                        weight = getMongDto.getWeight();
                        strength = getMongDto.getStrength();
                        fatigue = getMongDto.getFatigue();
                        mongTypeCode = getMongDto.getMongTypeCode();
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

        CreateMatchDto createMatchDto = matchService.createMatch(createMatchVoSet);

        return CreateBattleDto.builder()
                .roomId(createMatchDto.getRoomId())
                .battlePlayers(createMatchDto.getMatchPlayers())
                .build();
    }

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
