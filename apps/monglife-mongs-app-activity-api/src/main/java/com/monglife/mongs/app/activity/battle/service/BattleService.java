package com.monglife.mongs.app.activity.battle.service;

import com.monglife.mongs.app.activity.battle.domain.BattlePlayerEntity;
import com.monglife.mongs.app.activity.battle.domain.BattleRoomEntity;
import com.monglife.mongs.app.activity.battle.domain.BattleRoundEntity;
import com.monglife.mongs.app.activity.battle.domain.MongEntity;
import com.monglife.mongs.app.activity.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.FightBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.activity.battle.enums.BattleRoundCode;
import com.monglife.mongs.app.activity.battle.exception.*;
import com.monglife.mongs.app.activity.battle.repository.BattleRoomRepository;
import com.monglife.mongs.app.activity.battle.repository.MongRepository;
import com.monglife.mongs.app.activity.battle.vo.BattlePlayerVo;
import com.monglife.mongs.module.jpa.repository.ComnCodeRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
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

    private static final String MONG_TYPE_EGG_GROUP_CODE = "GCH100";

    @Value("${application.service.battle.max-round}")
    public Integer MAX_ROUND;

    @Value("${application.service.battle.max-random-origin}")
    private Double MAX_RANDOM_ORIGIN;

    @Value("${application.service.battle.max-random-bound}")
    private Double MAX_RANDOM_BOUND;

    private final ComnCodeRepository comnCodeRepository;

    private final BattleRoomRepository battleRoomRepository;

    private final MongRepository mongRepository;


    /**
     * 배틀 생성
     * @param createBattleDtoSet 매칭 플레이어 Set
     */
    @Transactional
    public Pair<Long, Set<BattlePlayerVo>> createBattle(Set<CreateBattleDto> createBattleDtoSet) {

        // 배틀 룸 엔티티 생성
        BattleRoomEntity battleRoomEntity = new BattleRoomEntity(MAX_ROUND);

        // 배틀 플레이어 엔티티 생성
        List<BattlePlayerEntity> battlePlayerEntities = createBattleDtoSet.stream()
                .map(createBattleDto -> {
                    String mongTypeCode;
                    double attackValue = BattlePlayerEntity.MAX_ATTACK_VALUE;
                    double healValue = BattlePlayerEntity.MAX_HEAL_VALUE;
                    double defenceValue = BattlePlayerEntity.MAX_DEFENCE_VALUE;

                    // 봇이 아닌 경우
                    if (!createBattleDto.getIsBot()) {

                        Long mongId = createBattleDto.getMongId();

                        MongEntity mongEntity = mongRepository.findById(mongId)
                                .orElseThrow(() -> new NotExistsMongIdException(mongId));

                        mongTypeCode = mongEntity.getMongTypeCode();
                        attackValue = BattlePlayerEntity.MAX_ATTACK_VALUE + (BattlePlayerEntity.MAX_ATTACK_VALUE * mongEntity.getStrengthRatio());
                        healValue = BattlePlayerEntity.MAX_HEAL_VALUE + (BattlePlayerEntity.MAX_HEAL_VALUE * mongEntity.getFatigue());
                        defenceValue = BattlePlayerEntity.MAX_DEFENCE_VALUE + (BattlePlayerEntity.MAX_DEFENCE_VALUE * mongEntity.getWeightRatio());

                    } else {

                        mongTypeCode = comnCodeRepository.findByGroupCode(MONG_TYPE_EGG_GROUP_CODE).stream()
                                .findAny()
                                .orElseThrow(NotExistsMongTypeCodeException::new)
                                .getComnCode();
                    }

                    return BattlePlayerEntity.builder()
                            .playerId(createBattleDto.getPlayerId())
                            .deviceId(createBattleDto.getDeviceId())
                            .accountId(createBattleDto.getAccountId())
                            .mongId(createBattleDto.getMongId())
                            .mongTypeCode(mongTypeCode)
                            .attackValue(attackValue / random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .healValue(healValue / random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .defenceValue(defenceValue / random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .isBot(createBattleDto.getIsBot())
                            .build();
                })
                .toList();

        battleRoomEntity.joinBattlePlayer(battlePlayerEntities);

        // 실제 플레이어 필터링
        Set<String> playerIdSet = createBattleDtoSet.stream()
                .filter(battlePlayerEntity -> !battlePlayerEntity.getIsBot())
                .map(CreateBattleDto::getPlayerId)
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

        return new Pair<>(battleRoomEntity.getRoomId(), battlePlayers);
    }

    /**
     * 배틀 입장
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     * @return 배틀 시작 라운드 정보
     */
    @Transactional
    public Pair<Boolean, FightBattleDto> enterBattle(Long roomId, String playerId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findByRoomIdAndIsActiveFalseAndRound(roomId, 0)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        battleRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 입장 처리
        battleRoomEntity.enterBattlePlayer(playerId);

        FightBattleDto fightBattleDto = null;
        Boolean isEnterAll = battleRoomEntity.isBattlePlayerEnterAll();

        if (isEnterAll) {
            Set<BattlePlayerVo> battlePlayers = battleRoomEntity.getBattlePlayerSet().stream()
                    .map(battlePlayerEntity -> BattlePlayerVo.of(battlePlayerEntity, BattleRoundCode.NONE))
                    .collect(Collectors.toSet());

            Integer round = battleRoomEntity.getRound();
            Boolean isLastRound = battleRoomEntity.isLastRound();

            fightBattleDto = FightBattleDto.builder()
                    .round(round)
                    .battlePlayers(battlePlayers)
                    .isLastRound(isLastRound)
                    .build();

            battleRoomEntity.start();
        }

        return new Pair<>(isEnterAll, fightBattleDto);
    }

    /**
     * 배틀 퇴장
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     */
    @Transactional
    public Pair<Boolean, List<OverBattleDto>> exitBattle(Long roomId, String playerId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        battleRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 퇴장 처리
        battleRoomEntity.excludeBattlePlayer(playerId);

        List<OverBattleDto> overBattleDtos = null;
        Boolean isExitAll = battleRoomEntity.isBattlePlayerExitAll();

        if (isExitAll) {
            Set<BattlePlayerEntity> battlePlayerEntities =  battleRoomEntity.getBattlePlayerSet();

            List<BattlePlayerEntity> rankBattlePlayerEntities = this.rankBattlePlayer(battlePlayerEntities);

            overBattleDtos = rankBattlePlayerEntities.stream()
                    .map(battlePlayerEntity -> OverBattleDto.builder()
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

        return new Pair<>(isExitAll, overBattleDtos);
    }

    /**
     * 배틀 선택
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     * @return  모든 플레이어 선택 완료 여부
     */
    @Transactional
    public Pair<Boolean, FightBattleDto> pickBattle(Long roomId, String playerId, String targetPlayerId, BattleRoundCode battleRoundCode) {

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
        FightBattleDto fightBattleDto = null;

        // 전체 선택 여부 확인
        if (isPickAll) {

            Set<BattlePlayerVo> battlePlayers = battleRoomEntity.nextRound();

            Boolean isLastRound = battleRoomEntity.isLastRound() || battleRoomEntity.isBattlePlayerDeadAll();

            fightBattleDto = FightBattleDto.builder()
                    .round(round)
                    .battlePlayers(battlePlayers)
                    .isLastRound(isLastRound)
                    .build();
        }

        return new Pair<>(isPickAll, fightBattleDto);
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
    public List<OverBattleDto> findOverBattle(Long roomId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findByRoomIdAndIsActiveFalse(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Set<BattlePlayerEntity> battlePlayerEntities =  battleRoomEntity.getBattlePlayerSet();

        List<BattlePlayerEntity> rankBattlePlayerEntities = this.rankBattlePlayer(battlePlayerEntities);

        return rankBattlePlayerEntities.stream()
                .map(battlePlayerEntity -> OverBattleDto.builder()
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
