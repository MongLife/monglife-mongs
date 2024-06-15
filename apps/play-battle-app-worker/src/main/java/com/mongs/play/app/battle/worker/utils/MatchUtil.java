package com.mongs.play.app.battle.worker.utils;

import com.mongs.play.app.battle.worker.vo.RoundResultVo;
import com.mongs.play.domain.battle.code.PickCode;
import com.mongs.play.domain.battle.vo.BattlePlayerVo;
import com.mongs.play.domain.battle.vo.BattleRoomVo;
import com.mongs.play.domain.battle.vo.BattleRoundVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MatchUtil {

    public static Map<String, RoundResultVo> setRoundResult(BattleRoomVo battleRoomVo) {
        // 플레이어 정보 map
        Map<String, BattlePlayerVo> battlePlayerVoMap = battleRoomVo.battlePlayerVoList().stream()
                .collect(Collectors.toMap(BattlePlayerVo::playerId, battlePlayerVo -> battlePlayerVo));

        // 라운드 결과 저장 vo map
        Map<String, RoundResultVo> roundResultVoMap = battleRoomVo.battlePlayerVoList().stream()
                .map(battlePlayerVo -> RoundResultVo.builder()
                        .playerId(battlePlayerVo.playerId())
                        .build())
                .collect(Collectors.toMap(RoundResultVo::getPlayerId, roundResultVo -> roundResultVo));

        /* 라운드 결과 정리
         * 방어, 회복인 경우에는 targetPlayerId 를 자기 자신으로 맞춰야함
         */
        int nowRound = battleRoomVo.round();
        List<BattleRoundVo> battleRoundVoList = battleRoomVo.battleRoundVoList().stream()
                .filter(battleRoundVo -> battleRoundVo.round() == nowRound)
                .toList();
        for (BattleRoundVo battleRoundVo : battleRoundVoList) {
            log.info("[round] {} : {} -> {}", battleRoundVo.pick(), battleRoundVo.playerId(), battleRoundVo.targetPlayerId());
            RoundResultVo roundResultVo = roundResultVoMap.get(battleRoundVo.targetPlayerId());
            if (battleRoundVo.pick().equals(PickCode.ATTACK)) {
                Double damage = battlePlayerVoMap.get(battleRoundVo.playerId()).attackValue();
                roundResultVo.setDamage(damage);
                roundResultVo.setAttacked(true);
            } else if (battleRoundVo.pick().equals(PickCode.DEFENCE)) {
                roundResultVo.setDefenced(true);
            } else {
                roundResultVo.setHealed(true);
            }
        }

        return roundResultVoMap;
    }

    public static boolean validateMatchPick(List<String> playerIdList, List<String> roundPlayerIdList) {
        for (String playerId : playerIdList) {
            if (!roundPlayerIdList.contains(playerId)) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateMatchPlayerZeroHp(List<BattlePlayerVo> battlePlayerVoList) {
        for (BattlePlayerVo battlePlayerVo : battlePlayerVoList) {
            if (battlePlayerVo.hp() <= 0) {
                return true;
            }
        }
        return false;
    }
}
