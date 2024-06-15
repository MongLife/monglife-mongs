package com.mongs.play.domain.battle.service;

import com.mongs.play.core.error.domain.BattleErrorCode;
import com.mongs.play.core.exception.common.InvalidException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.battle.code.PickCode;
import com.mongs.play.domain.battle.entity.BattlePlayer;
import com.mongs.play.domain.battle.entity.BattleRoom;
import com.mongs.play.domain.battle.entity.BattleRound;
import com.mongs.play.domain.battle.repository.BattlePlayerRepository;
import com.mongs.play.domain.battle.repository.BattleRoomRepository;
import com.mongs.play.domain.battle.vo.BattlePlayerVo;
import com.mongs.play.domain.battle.vo.BattleRoomVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRoomRepository battleRoomRepository;
    private final BattlePlayerRepository battlePlayerRepository;

    @Transactional(transactionManager = "battleTransactionManager", readOnly = true)
    public BattlePlayerVo getBattlePlayer(String playerId) {
        BattlePlayer battlePlayer = battlePlayerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_PLAYER));
        return BattlePlayerVo.of(battlePlayer);
    }

    @Transactional(transactionManager = "battleTransactionManager", readOnly = true)
    public BattleRoomVo getBattleRoom(String roomId) {
        BattleRoom battleRoom = battleRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_BATTLE));
        return BattleRoomVo.of(battleRoom);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattleRoomVo addBattleRoom() {
        BattleRoom battleRoom = BattleRoom.builder()
                .isActive(true)
                .build();

        battleRoom = battleRoomRepository.save(battleRoom);

        return BattleRoomVo.of(battleRoom);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattlePlayerVo addBattlePlayer(String playerId, Long mongId, String mongCode, Double attackValue, Double healValue, Boolean isBot) {
        BattlePlayer battlePlayer = BattlePlayer.builder()
                .playerId(playerId)
                .mongId(mongId)
                .mongCode(mongCode)
                .hp(500D)
                .attackValue(attackValue)
                .healValue(healValue)
                .isBot(isBot)
                .build();

        battlePlayer = battlePlayerRepository.save(battlePlayer);

        return BattlePlayerVo.of(battlePlayer);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattleRoomVo addBattleRound(String roomId, String playerId, String targetPlayerId, PickCode pick) {
        BattleRoom battleRoom = battleRoomRepository.findByRoomIdWithLock(roomId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_BATTLE));

        BattleRound battleRound = BattleRound.builder()
                .round(battleRoom.getRound())
                .playerId(playerId)
                .targetPlayerId(targetPlayerId)
                .pick(pick)
                .build();

        battleRoom.addBattleRound(battleRound);
        battleRoomRepository.save(battleRoom);

        return BattleRoomVo.of(battleRoom);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattleRoomVo enterBattleRoom(String roomId, String playerId) {
        BattleRoom battleRoom = battleRoomRepository.findByRoomIdWithLock(roomId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_BATTLE));

        BattlePlayer battlePlayer = battlePlayerRepository.findByPlayerIdWithLock(playerId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_PLAYER));

        battleRoom.addBattlePlayer(battlePlayer);
        battleRoomRepository.save(battleRoom);
        return BattleRoomVo.of(battleRoom);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattleRoomVo exitBattleRoom(String roomId, String playerId) {
        BattleRoom battleRoom = battleRoomRepository.findByRoomIdWithLock(roomId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_BATTLE));

        Set<String> battlePlayerIdList = battleRoom.getBattlePlayerList().stream()
                .map(BattlePlayer::getPlayerId)
                .collect(Collectors.toSet());

        if (!battlePlayerIdList.contains(playerId)) {
            throw new NotFoundException(BattleErrorCode.NOT_FOUND_PLAYER);
        }

        BattlePlayer battlePlayer = battlePlayerRepository.findByPlayerIdWithLock(playerId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_PLAYER));

        battleRoom.removeBattlePlayer(battlePlayer);
        battleRoomRepository.save(battleRoom);
        return BattleRoomVo.of(battleRoom);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public void removeBattle(String roomId) {
        BattleRoom battleRoom = battleRoomRepository.findByRoomIdWithLock(roomId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_BATTLE));

        battleRoomRepository.save(battleRoom.toBuilder()
                .isActive(false)
                .build());
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattlePlayerVo healPlayer(String playerId) {
        BattlePlayer battlePlayer = battlePlayerRepository.findByPlayerIdWithLock(playerId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_PLAYER));

        battlePlayer.heal();
        battlePlayerRepository.save(battlePlayer);
        return BattlePlayerVo.of(battlePlayer);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattlePlayerVo attackPlayer(String playerId, Double damage) {
        BattlePlayer battlePlayer = battlePlayerRepository.findByPlayerIdWithLock(playerId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_PLAYER));

        battlePlayer.attacked(damage);
        battlePlayerRepository.save(battlePlayer);
        return BattlePlayerVo.of(battlePlayer);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattlePlayerVo attackWithHealPlayer(String playerId, Double damage) {
        BattlePlayer battlePlayer = battlePlayerRepository.findByPlayerIdWithLock(playerId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_PLAYER));

        battlePlayer.attackedWithHeal(damage);
        battlePlayerRepository.save(battlePlayer);
        return BattlePlayerVo.of(battlePlayer);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattleRoomVo increaseRound(String roomId) {
        BattleRoom battleRoom = battleRoomRepository.findByRoomIdWithLock(roomId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_BATTLE));

        battleRoom = battleRoomRepository.save(battleRoom.toBuilder()
                .round(battleRoom.getRound() + 1)
                .build());
        return BattleRoomVo.of(battleRoom);
    }
}
