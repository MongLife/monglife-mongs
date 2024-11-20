package com.monglife.mongs.app.battle.service;

import com.monglife.core.enums.common.MongCode;
import com.monglife.mongs.app.battle.domain.BattlePlayerEntity;
import com.monglife.mongs.app.battle.domain.BattleRoomEntity;
import com.monglife.mongs.app.battle.domain.BattleRoundEntity;
import com.monglife.mongs.app.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.battle.dto.etc.FightBattleDto;
import com.monglife.mongs.app.battle.dto.etc.FindMatchingDto;
import com.monglife.mongs.app.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.battle.global.enums.BattleRoundCode;
import com.monglife.mongs.app.battle.global.exception.*;
import com.monglife.mongs.app.battle.repository.BattleRoomRepository;
import com.monglife.mongs.app.battle.vo.BattlePlayerVo;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService {

    private static final Random random = new Random();

    private static final Integer MAX_ROUND = 10;
    private static final Double MAX_RANDOM_ORIGIN = 1.0;
    private static final Double MAX_RANDOM_BOUND = 2.0;
    private static final Double MAX_HP = 500D;
    private static final Double MAX_ATTACK_VALUE = 50D;
    private static final Double MAX_HEAL_VALUE = 25D;
    private static final Double MAX_DEFENCE_VALUE = 15D;

    private final BattleRoomRepository battleRoomRepository;


    /**
     * 배틀 생성
     * @param createBattleDtoSet 매칭 플레이어 Set
     */
    @Transactional
    public Pair<Long, Set<BattlePlayerVo>> createBattle(Set<CreateBattleDto> createBattleDtoSet) {

        // 실제 플레이어 필터링
        Set<String> playerIdSet = createBattleDtoSet.stream()
                .filter(battlePlayerEntity -> !battlePlayerEntity.getIsBot())
                .map(CreateBattleDto::getPlayerId)
                .collect(Collectors.toSet());

        // 배틀 룸 엔티티 생성
        BattleRoomEntity battleRoomEntity = new BattleRoomEntity();

        // 배틀 플레이어 엔티티 생성
        List<BattlePlayerEntity> battlePlayerEntities = createBattleDtoSet.stream()
                .map(createBattleDto -> {
//                    double attackValue = DEFAULT_ATTACK_VALUE + (mongVo.strength() / mongVo.grade().maxStatus * 100D);
//                    double healValue = DEFAULT_HEAL_VALUE + (mongVo.sleep() / mongVo.grade().maxStatus * 100D);
//                    double defenceValue = DEFAULT_DEFENCE_VALUE + (mongVo.weight() / mongVo.grade().maxStatus * 100D);

                    String mongCode = MongCode.CH100.getCode();
                    Double attackValue = MAX_ATTACK_VALUE;
                    Double healValue = MAX_HEAL_VALUE;
                    Double defenceValue = MAX_DEFENCE_VALUE;

                    // 봇이 아닌 경우
                    if (!createBattleDto.getIsBot()) {
                        mongCode = MongCode.CH200.getCode();    // TODO: Feign Client 등록 후, Management 정보 받아 와야 함
                        attackValue = MAX_ATTACK_VALUE;         // TODO: Feign Client 등록 후, Management 정보 받아 와야 함
                        healValue = MAX_HEAL_VALUE;             // TODO: Feign Client 등록 후, Management 정보 받아 와야 함
                        defenceValue = MAX_DEFENCE_VALUE;       // TODO: Feign Client 등록 후, Management 정보 받아 와야 함
                    }

                    return BattlePlayerEntity.builder()
                            .playerId(createBattleDto.getPlayerId())
                            .deviceId(createBattleDto.getDeviceId())
                            .accountId(createBattleDto.getAccountId())
                            .mongId(createBattleDto.getMongId())
                            .mongCode(mongCode)
                            .hp(MAX_HP)
                            .attackValue(attackValue / random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .healValue(healValue / random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .defenceValue(defenceValue / random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .isBot(createBattleDto.getIsBot())
                            .build();
                })
                .toList();

        battleRoomEntity.joinBattlePlayer(battlePlayerEntities);

        // 봇인 경우 MAX_ROUND 만큼의 round 미리 생성
        battlePlayerEntities.forEach(battlePlayerEntity -> {
            // 봇인 경우
            if (battlePlayerEntity.getIsBot()) {
                // 입장 처리
                battlePlayerEntity.enter();

                List<BattleRoundEntity> battleRoundEntities = new ArrayList<>();

                for (int round = 1; round <= MAX_ROUND; round++) {

                    // 선택 코드
                    List<BattleRoundCode> battleRoundCodes = Arrays.stream(BattleRoundCode.values())
                            .filter(battleRoundCode -> battleRoundCode.name().startsWith("BATTLE_PICK_"))
                            .toList();
                    int battleRoundCodeIndex = random.nextInt(battleRoundCodes.size());
                    BattleRoundCode botRoundCode = battleRoundCodes.get(battleRoundCodeIndex);

                    // 상대 playerId
                    int targetPlayerIdIndex = random.nextInt(playerIdSet.size());
                    String targetPlayerId = playerIdSet.stream()
                            .skip(targetPlayerIdIndex)
                            .findFirst()
                            .orElseThrow(() -> new OnlyBotMatchingException(playerIdSet));

                    BattleRoundEntity battleRoundEntity = switch (botRoundCode) {
                        case BATTLE_PICK_DEFENCE -> BattleRoundEntity.builder()
                                    .playerId(battlePlayerEntity.getPlayerId())
                                    .targetPlayerId(targetPlayerId)
                                    .round(round)
                                    .roundCode(botRoundCode)
                                    .roundValue(battlePlayerEntity.getDefenceValue())
                                    .build();
                        case BATTLE_PICK_HEAL -> BattleRoundEntity.builder()
                                    .playerId(battlePlayerEntity.getPlayerId())
                                    .targetPlayerId(targetPlayerId)
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

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        BattlePlayerEntity enterBattlePlayerEntity = battleRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 입장 처리
        enterBattlePlayerEntity.enter();

        FightBattleDto fightBattleDto = null;
        Boolean isEnterAll = battleRoomEntity.isBattlePlayerEnterAll();

        if (isEnterAll) {
            battleRoomEntity.start();
            // 0 -> 1
            battleRoomEntity.increaseRound();

            Integer round = battleRoomEntity.getRound();
            Boolean isLastRound = MAX_ROUND.equals(round);

            Set<BattlePlayerVo> battlePlayers = battleRoomEntity.getBattlePlayerSet().stream()
                    .map(battlePlayerEntity -> BattlePlayerVo.of(battlePlayerEntity, BattleRoundCode.NONE))
                    .collect(Collectors.toSet());

            fightBattleDto = FightBattleDto.builder()
                    .round(round)
                    .battlePlayers(battlePlayers)
                    .isLastRound(isLastRound)
                    .build();
        }

        return new Pair<>(isEnterAll, fightBattleDto);
    }

    /**
     * 배틀 퇴장
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     */
    @Transactional
    public Boolean exitBattle(Long roomId, String playerId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        BattlePlayerEntity battlePlayerEntity = battleRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        battlePlayerEntity.exit();

        return battleRoomEntity.isBattlePlayerExitAll();
    }

    /**
     * 배틀 선택
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     * @return  모든 플레이어 선택 완료 여부
     */
    @Transactional
    public Boolean pickBattle(Long roomId, String playerId, String targetPlayerId, BattleRoundCode battleRoundCode) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findById(roomId)
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

        // 전체 선택 여부 확인
        return battleRoomEntity.isBattleRoundPickAll();
    }

    /**
     * 배틀 종료
     * @param roomId 배틀룸 ID
     * @return 등수별 정렬한 OverBattleDto 목록
     */
    @Transactional
    public List<OverBattleDto> overBattle(Long roomId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Set<BattlePlayerEntity> battlePlayerEntities =  battleRoomEntity.getBattlePlayerSet();

        List<BattlePlayerEntity> rankBattlePlayerEntities = this.rankBattlePlayer(battlePlayerEntities);

        battleRoomEntity.over();

        return rankBattlePlayerEntities.stream()
                .map(battlePlayerEntity -> {
                    battlePlayerEntity.exit();

                    return OverBattleDto.builder()
                            .playerId(battlePlayerEntity.getPlayerId())
                            .mongId(battlePlayerEntity.getMongId())
                            .mongCode(battlePlayerEntity.getMongCode())
                            .build();
                })
                .toList();
    }

    /**
     * 배틀 라운드 진행
     * @param roomId 배틀룸 ID
     * @return 배틀 종료 여부
     */
    @Transactional
    public FightBattleDto fightBattle(Long roomId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Integer round = battleRoomEntity.getRound();
        Boolean isLastRound = MAX_ROUND.equals(round);

        Set<BattlePlayerVo> battlePlayers = null;           // TODO: 라운드 진행 로직 작성

        return FightBattleDto.builder()
                .round(round)
                .battlePlayers(battlePlayers)
                .isLastRound(isLastRound)
                .build();
    }

    /**
     * 종료 된 배틀 결과 조회
     * @param roomId 배틀룸 ID
     * @return 등수별 정렬한 OverBattleDto 목록
     */
    @Transactional
    public List<OverBattleDto> findOverBattle(Long roomId) {

        BattleRoomEntity battleRoomEntity = battleRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Set<BattlePlayerEntity> battlePlayerEntities =  battleRoomEntity.getBattlePlayerSet();

        List<BattlePlayerEntity> rankBattlePlayerEntities = this.rankBattlePlayer(battlePlayerEntities);

        return rankBattlePlayerEntities.stream()
                .map(battlePlayerEntity -> OverBattleDto.builder()
                            .playerId(battlePlayerEntity.getPlayerId())
                            .mongId(battlePlayerEntity.getMongId())
                            .mongCode(battlePlayerEntity.getMongCode())
                            .build())
                .toList();
    }

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
                // 순위 리스트에 앞에서부터 삽입 (하위 등수 부터 저장)
                .forEachOrdered(battlePlayerEntity -> rankBattlePlayerEntities.add(0, battlePlayerEntity));

        return rankBattlePlayerEntities;
    }
}
