package com.monglife.mongs.app.battle.service;

import com.monglife.mongs.app.battle.domain.BattlePlayerEntity;
import com.monglife.mongs.app.battle.domain.BattleRoomEntity;
import com.monglife.mongs.app.battle.domain.BattleRoundEntity;
import com.monglife.mongs.app.battle.dto.etc.*;
import com.monglife.mongs.app.battle.repository.BattleRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService {

    private static final Integer MAX_ROUND = 10;

    private final BattleRoomRepository battleRoomRepository;


    /**
     * 배틀 생성
     * @param createBattleDtoSet 매칭 플레이어 Set
     */
    @Transactional
    public void createBattle(Set<CreateBattleDto> createBattleDtoSet) {

        // 배틀 룸 엔티티 생성
        BattleRoomEntity battleRoomEntity = new BattleRoomEntity();

        // 배틀 플레이어 엔티티 생성
        List<BattlePlayerEntity> battlePlayerEntities = createBattleDtoSet.stream()
                .map(createBattleDto -> BattlePlayerEntity.builder()
                            .accountId(createBattleDto.getAccountId())
                            .mongId(createBattleDto.getMongId())
//                            .mongCode()               // TODO: Feign Client 등록 후, Management 정보 받아 와야 함
//                            .hp()                     // TODO: Feign Client 등록 후, Management 정보 받아 와야 함
//                            .attackValue()            // TODO: Feign Client 등록 후, Management 정보 받아 와야 함
//                            .healValue()              // TODO: Feign Client 등록 후, Management 정보 받아 와야 함
//                            .defenceValue()           // TODO: Feign Client 등록 후, Management 정보 받아 와야 함
                            .isBot(createBattleDto.getIsBot())
                            .build())
                .toList();

        // 실제 플레이어 필터링
        Set<String> playerIdSet = battlePlayerEntities.stream()
                        .filter(battlePlayerEntity -> !battlePlayerEntity.getIsBot())
                        .map(BattlePlayerEntity::getPlayerId)
                        .collect(Collectors.toSet());

        battlePlayerEntities.forEach(battlePlayerEntity -> {
            if (battlePlayerEntity.getIsBot()) {

                List<BattleRoundEntity> battleRoundEntities = new ArrayList<>();

                for (int round = 1; round <= MAX_ROUND; round++) {

                    String targetPlayerId = playerIdSet.st

                    BattleRoundEntity battleRoundEntity = BattleRoundEntity.builder()
                            .playerId(battlePlayerEntity.getPlayerId())
                            .targetPlayerId(playerIdSet.)
                            .build();

                    battleRoundEntities.add()
                }
            }
        });

        battleRoomEntity.joinBattlePlayer(battlePlayerEntities);

        battleRoomRepository.save(battleRoomEntity);
    }

    /**
     * 배틀 입장
     * @param enterBattleDto 배틀 입장 Dto
     */
    @Transactional
    public void enterBattle(EnterBattleDto enterBattleDto) {

    }

    /**
     * 배틀 퇴장
     * @param exitBattleDto 배틀 퇴장 Dto
     */
    @Transactional
    public void exitBattle(ExitBattleDto exitBattleDto) {

    }

    /**
     * 배틀 종료
     * @param overBattleDto 배틀 종료 Dto
     */
    @Transactional
    public void overBattle(OverBattleDto overBattleDto) {

    }

    /**
     * 배틀 선택
     * @param pickBattleDto 배틀 선택 Dto
     */
    @Transactional
    public void pickBattle(PickBattleDto pickBattleDto) {

    }
}
