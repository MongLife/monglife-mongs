package com.mongs.play.domain.battle.service;

import com.mongs.play.domain.battle.entity.BattlePlayer;
import com.mongs.play.domain.battle.entity.BattleRoom;
import com.mongs.play.domain.battle.repository.BattleRoomRepository;
import com.mongs.play.domain.battle.vo.BattlePlayerVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRoomRepository battleRoomRepository;

    @Transactional(transactionManager = "battleTransactionManager")
    public void addBattleRoom(List<BattlePlayerVo> battlePlayerVoList) {

        BattleRoom battleRoom = BattleRoom.builder().build();

        battlePlayerVoList.forEach(battlePlayerVo -> {
            battleRoom.addBattlePlayer(BattlePlayer.builder()
                    .mongId(battlePlayerVo.mongId())
                    .mongCode(battlePlayerVo.mongCode())
                    .attackValue(battlePlayerVo.attackValue())
                    .healValue(battlePlayerVo.healValue())
                    .build());
        });

        battleRoomRepository.save(battleRoom);
    }
}
