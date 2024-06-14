package com.mongs.play.domain.battle.service;

import com.mongs.play.core.error.domain.BattleErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.battle.entity.BattlePlayer;
import com.mongs.play.domain.battle.entity.BattleRoom;
import com.mongs.play.domain.battle.repository.BattlePlayerRepository;
import com.mongs.play.domain.battle.repository.BattleRoomRepository;
import com.mongs.play.domain.battle.vo.BattlePlayerVo;
import com.mongs.play.domain.battle.vo.BattleRoomVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRoomRepository battleRoomRepository;
    private final BattlePlayerRepository battlePlayerRepository;

    @Transactional(transactionManager = "battleTransactionManager")
    public BattleRoomVo addBattleRoom() {
        BattleRoom battleRoom = BattleRoom.builder().build();
        battleRoomRepository.save(battleRoom);
        return BattleRoomVo.of(battleRoom);
    }

    @Transactional(transactionManager = "battleTransactionManager")
    public BattlePlayerVo addBattlePlayer(BattlePlayerVo battlePlayerVo) {
        BattlePlayer battlePlayer = BattlePlayer.builder()
                .playerId(battlePlayerVo.playerId())
                .mongId(battlePlayerVo.mongId())
                .hp(500D)
                .mongCode(battlePlayerVo.mongCode())
                .attackValue(battlePlayerVo.attackValue())
                .healValue(battlePlayerVo.healValue())
                .build();

        battlePlayerRepository.save(battlePlayer);

        return BattlePlayerVo.of(battlePlayer);
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

        BattlePlayer battlePlayer = battlePlayerRepository.findByPlayerIdWithLock(playerId)
                .orElseThrow(() -> new NotFoundException(BattleErrorCode.NOT_FOUND_PLAYER));

        battleRoom.removeBattlePlayer(battlePlayer);

        battleRoomRepository.save(battleRoom);

        return BattleRoomVo.of(battleRoom);
    }
}
