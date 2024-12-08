package com.monglife.mongs.domain.battle.service;

import com.monglife.mongs.domain.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.domain.battle.dto.etc.EnterBattleDto;
import com.monglife.mongs.domain.battle.dto.etc.ExitBattleDto;
import com.monglife.mongs.domain.battle.dto.etc.PickBattleDto;
import com.monglife.mongs.domain.battle.entity.BattlePlayerEntity;
import com.monglife.mongs.domain.battle.entity.BattleRoomEntity;
import com.monglife.mongs.domain.battle.entity.BattleRoundEntity;
import com.monglife.mongs.domain.battle.enums.BattleRoundCode;
import com.monglife.mongs.domain.battle.exception.AlreadyExistsRoundException;
import com.monglife.mongs.domain.battle.exception.NotExistsPlayerIdException;
import com.monglife.mongs.domain.battle.exception.NotExistsRoomIdException;
import com.monglife.mongs.domain.battle.exception.OnlyBotMatchingException;
import com.monglife.mongs.domain.battle.repository.BattleRoomRepository;
import com.monglife.mongs.domain.battle.vo.BattlePlayerVo;
import com.monglife.mongs.domain.battle.vo.CreateBattleVo;
import com.monglife.mongs.domain.battle.vo.FightBattleVo;
import com.monglife.mongs.domain.battle.vo.OverBattleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService {

    private static final Random random = new Random();

    @Value("${application.service.battle.max-round}")
    public Integer MAX_ROUND;

    @Value("${application.service.battle.max-random-origin}")
    private Double MAX_RANDOM_ORIGIN;

    @Value("${application.service.battle.max-random-bound}")
    private Double MAX_RANDOM_BOUND;

    private final BattleRoomRepository battleRoomRepository;


    /**
     * 배틀 생성
     * @param createBattleVoSet 매칭 플레이어 Set
     */
    @Transactional
    public CreateBattleDto createBattle(Set<CreateBattleVo> createBattleVoSet) {

        // 배틀 룸 엔티티 생성
        BattleRoomEntity battleRoomEntity = new BattleRoomEntity(MAX_ROUND);

        // 배틀 플레이어 엔티티 생성
        List<BattlePlayerEntity> battlePlayerEntities = createBattleVoSet.stream()
                .map(createBattleVo -> {
                    double defenceValue = BattlePlayerEntity.MAX_DEFENCE_VALUE;
                    double attackValue = BattlePlayerEntity.MAX_ATTACK_VALUE;
                    double healValue = BattlePlayerEntity.MAX_HEAL_VALUE;

                    // 봇이 아닌 경우
                    if (!createBattleVo.getIsBot()) {
                        defenceValue = BattlePlayerEntity.MAX_DEFENCE_VALUE + createBattleVo.getWeight();
                        attackValue = BattlePlayerEntity.MAX_ATTACK_VALUE + createBattleVo.getStrength();
                        healValue = BattlePlayerEntity.MAX_HEAL_VALUE + createBattleVo.getFatigue();
                    }

                    return BattlePlayerEntity.builder()
                            .playerId(createBattleVo.getPlayerId())
                            .deviceId(createBattleVo.getDeviceId())
                            .accountId(createBattleVo.getAccountId())
                            .mongId(createBattleVo.getMongId())
                            .mongTypeCode(createBattleVo.getMongTypeCode())
                            .defenceValue(defenceValue * random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .attackValue(attackValue * random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .healValue(healValue * random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .isBot(createBattleVo.getIsBot())
                            .build();
                })
                .toList();

        battleRoomEntity.joinBattlePlayer(battlePlayerEntities);

        // 실제 플레이어 필터링
        Set<String> playerIdSet = createBattleVoSet.stream()
                .filter(battlePlayerEntity -> !battlePlayerEntity.getIsBot())
                .map(CreateBattleVo::getPlayerId)
                .collect(Collectors.toSet());

        // 봇인 경우 MAX_ROUND 만큼의 round 미리 생성
        battlePlayerEntities.forEach(battlePlayerEntity -> {
            // 봇인 경우
            if (battlePlayerEntity.getIsBot()) {
                List<BattleRoundEntity> battleRoundEntities = new ArrayList<>();

                for (int round = 1; round <= battleRoomEntity.getMaxRound(); round++) {

                    // 선택 코드
                    List<BattleRoundCode> battleRoundCodes
                            = List.of(BattleRoundCode.BATTLE_PICK_ATTACK, BattleRoundCode.BATTLE_PICK_DEFENCE, BattleRoundCode.BATTLE_PICK_HEAL);

                    int battleRoundCodeIndex = random.nextInt(battleRoundCodes.size());
                    BattleRoundCode botRoundCode = battleRoundCodes.get(battleRoundCodeIndex);

                    // 봇 playerId
                    String playerId = battlePlayerEntity.getPlayerId();

                    // 상대 playerId
                    int targetPlayerIdIndex = random.nextInt(playerIdSet.size());
                    String targetPlayerId = playerIdSet.stream()
                            .skip(targetPlayerIdIndex)
                            .findFirst()
                            .orElseThrow(() -> new OnlyBotMatchingException(playerIdSet));

                    BattleRoundEntity battleRoundEntity = switch (botRoundCode) {
                        case BATTLE_PICK_DEFENCE -> BattleRoundEntity.builder()
                                    .playerId(battlePlayerEntity.getPlayerId())
                                    .targetPlayerId(playerId)
                                    .round(round)
                                    .roundCode(botRoundCode)
                                    .roundValue(battlePlayerEntity.getDefenceValue())
                                    .build();
                        case BATTLE_PICK_HEAL -> BattleRoundEntity.builder()
                                    .playerId(battlePlayerEntity.getPlayerId())
                                    .targetPlayerId(playerId)
                                    .round(round)
                                    .roundCode(botRoundCode)
                                    .roundValue(battlePlayerEntity.getHealValue())
                                    .build();
                        default -> BattleRoundEntity.builder()
                                    .playerId(battlePlayerEntity.getPlayerId())
                                    .targetPlayerId(targetPlayerId)
                                    .round(round)
                                    .roundCode(botRoundCode)
                                    .roundValue(battlePlayerEntity.getAttackValue())
                                    .build();
                    };

                    battleRoundEntities.add(battleRoundEntity);
                }

                battleRoomEntity.joinBattleRound(battleRoundEntities);
            }
        });

        battleRoomRepository.save(battleRoomEntity);

        Set<BattlePlayerVo> battlePlayers = battlePlayerEntities.stream()
                .map(battlePlayerEntity -> BattlePlayerVo.of(battlePlayerEntity, BattleRoundCode.NONE))
                .collect(Collectors.toSet());

        return CreateBattleDto.builder()
                .roomId(battleRoomEntity.getRoomId())
                .battlePlayers(battlePlayers)
                .build();
    }

    /**
     * 배틀 입장
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     * @return 배틀 시작 라운드 정보
     */
    @Transactional
    public EnterBattleDto enterBattle(Long roomId, String playerId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findByRoomIdAndIsActiveFalseAndRound(roomId, 0)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        battleRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 입장 처리
        battleRoomEntity.enterBattlePlayer(playerId);

        FightBattleVo fightBattleVo = null;
        Boolean isEnterAll = battleRoomEntity.isBattlePlayerEnterAll();

        if (isEnterAll) {
            Set<BattlePlayerVo> battlePlayers = battleRoomEntity.getBattlePlayerSet().stream()
                    .map(battlePlayerEntity -> BattlePlayerVo.of(battlePlayerEntity, BattleRoundCode.NONE))
                    .collect(Collectors.toSet());

            Integer round = battleRoomEntity.getRound();
            Boolean isLastRound = battleRoomEntity.isLastRound();

            fightBattleVo = FightBattleVo.builder()
                    .round(round)
                    .battlePlayers(battlePlayers)
                    .isLastRound(isLastRound)
                    .build();

            battleRoomEntity.start();
        }

        return EnterBattleDto.builder()
                .isEnterAll(isEnterAll)
                .fightBattleVo(fightBattleVo)
                .build();
    }

    /**
     * 배틀 퇴장
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     */
    @Transactional
    public ExitBattleDto exitBattle(Long roomId, String playerId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        battleRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 퇴장 처리
        battleRoomEntity.excludeBattlePlayer(playerId);

        List<OverBattleVo> overBattleVos = null;
        Boolean isExitAll = battleRoomEntity.isBattlePlayerExitAll();

        if (isExitAll) {
            Set<BattlePlayerEntity> battlePlayerEntities =  battleRoomEntity.getBattlePlayerSet();

            List<BattlePlayerEntity> rankBattlePlayerEntities = this.rankBattlePlayer(battlePlayerEntities);

            overBattleVos = rankBattlePlayerEntities.stream()
                    .map(battlePlayerEntity -> OverBattleVo.builder()
                                .playerId(battlePlayerEntity.getPlayerId())
                                .mongId(battlePlayerEntity.getMongId())
                                .mongTypeCode(battlePlayerEntity.getMongTypeCode())
                                .build())
                    .toList();
        } else {
            Integer round = battleRoomEntity.getRound();

            // 현재 라운드 선택한 경우
            if (battleRoomEntity.getCurrentBattleRound(playerId).isPresent()) {
                round = round + 1;
            }

            round = Math.min(round, battleRoomEntity.getMaxRound());

            while (round <= battleRoomEntity.getMaxRound()) {

                BattleRoundEntity battleRoundEntity = BattleRoundEntity.builder()
                        .playerId(playerId)
                        .targetPlayerId(playerId)
                        .round(round)
                        .roundCode(BattleRoundCode.NONE)
                        .roundValue(0D)
                        .build();

                battleRoomEntity.joinBattleRound(battleRoundEntity);

                round++;
            }
        }

        return ExitBattleDto.builder()
                .isExitAll(isExitAll)
                .overBattleVos(overBattleVos)
                .build();
    }

    /**
     * 배틀 선택
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     * @return  모든 플레이어 선택 완료 여부
     */
    @Transactional
    public PickBattleDto pickBattle(Long roomId, String playerId, String targetPlayerId, BattleRoundCode battleRoundCode) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Integer round = battleRoomEntity.getRound();

        // 선택 여부 확인
        battleRoomEntity.getCurrentBattleRound(playerId)
                .ifPresent(battleRoundEntity -> {
                    throw new AlreadyExistsRoundException(round, playerId);
                });

        // 현재 플레이어
        BattlePlayerEntity battlePlayerEntity = battleRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 타켓 플레이어 존재 확인
        battleRoomEntity.getBattlePlayer(targetPlayerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(targetPlayerId));

        BattleRoundEntity battleRoundEntity = switch (battleRoundCode) {
            case BATTLE_PICK_DEFENCE -> BattleRoundEntity.builder()
                    .playerId(playerId)
                    .targetPlayerId(targetPlayerId)
                    .round(round)
                    .roundCode(battleRoundCode)
                    .roundValue(battlePlayerEntity.getDefenceValue())
                    .build();
            case BATTLE_PICK_HEAL -> BattleRoundEntity.builder()
                    .playerId(playerId)
                    .targetPlayerId(targetPlayerId)
                    .round(round)
                    .roundCode(battleRoundCode)
                    .roundValue(battlePlayerEntity.getHealValue())
                    .build();
            default -> BattleRoundEntity.builder()
                    .playerId(playerId)
                    .targetPlayerId(targetPlayerId)
                    .round(round)
                    .roundCode(battleRoundCode)
                    .roundValue(battlePlayerEntity.getAttackValue())
                    .build();
        };

        // 라운드 등록
        battleRoomEntity.joinBattleRound(battleRoundEntity);

        Boolean isPickAll = battleRoomEntity.isBattleRoundPickAll();
        FightBattleVo fightBattleVo = null;

        // 전체 선택 여부 확인
        if (isPickAll) {

            Set<BattlePlayerVo> battlePlayers = battleRoomEntity.nextRound();

            Boolean isLastRound = battleRoomEntity.isLastRound() || battleRoomEntity.isBattlePlayerDeadAll();

            fightBattleVo = FightBattleVo.builder()
                    .round(round)
                    .battlePlayers(battlePlayers)
                    .isLastRound(isLastRound)
                    .build();
        }

        return PickBattleDto.builder()
                .isPickAll(isPickAll)
                .fightBattleVo(fightBattleVo)
                .build();
    }

    /**
     * 배틀 종료
     * @param roomId 배틀룸 ID
     */
    @Transactional
    public void overBattle(Long roomId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        battleRoomEntity.over();
    }

    /**
     * 종료 된 배틀 결과 조회
     * @param roomId 배틀룸 ID
     * @return 등수별 정렬한 OverBattleDto 목록
     */
    @Transactional
    public List<OverBattleVo> findOverBattle(Long roomId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findByRoomIdAndIsActiveFalse(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Set<BattlePlayerEntity> battlePlayerEntities =  battleRoomEntity.getBattlePlayerSet();

        List<BattlePlayerEntity> rankBattlePlayerEntities = this.rankBattlePlayer(battlePlayerEntities);

        return rankBattlePlayerEntities.stream()
                .map(battlePlayerEntity -> OverBattleVo.builder()
                            .playerId(battlePlayerEntity.getPlayerId())
                            .mongId(battlePlayerEntity.getMongId())
                            .mongTypeCode(battlePlayerEntity.getMongTypeCode())
                            .build())
                .toList();
    }

    /**
     * 배틀 플레이어 랭킹
     * @param battlePlayerEntities 배틀 플레이어 엔티티 목록
     * @return 배틀 플레이어 엔티티 등수 기준 정렬 리스트
     */
    private List<BattlePlayerEntity> rankBattlePlayer(Set<BattlePlayerEntity> battlePlayerEntities) {

        // 나간 배틀 플레이어
        List<BattlePlayerEntity> rankBattlePlayerEntities = battlePlayerEntities.stream()
                .filter(battlePlayerEntity -> !battlePlayerEntity.getIsEnter())
                .sorted((bp1, bp2) -> {
                    if (bp1.getHp().equals(bp2.getHp())) {
                        return bp1.getExitDt().compareTo(bp2.getExitDt());
                    }
                    return bp2.getHp().compareTo(bp1.getHp());
                })
                .collect(Collectors.toList());

        // 나가지 않은 배틀 플레이어
        battlePlayerEntities.stream()
                .filter(BattlePlayerEntity::getIsEnter)
                // 역순 정렬
                .sorted((bp1, bp2) -> {
                    if (bp1.getHp().equals(bp2.getHp())) {
                        return bp2.getEnterDt().compareTo(bp1.getEnterDt());
                    }
                    return bp1.getHp().compareTo(bp2.getHp());
                })
                // 순위 리스트 앞에서 부터 삽입 (하위 등수 부터 저장)
                .forEachOrdered(battlePlayerEntity -> rankBattlePlayerEntities.add(0, battlePlayerEntity));

        return rankBattlePlayerEntities;
    }
}
