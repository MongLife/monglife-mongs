package com.monglife.mongs.domain.match.service;

import com.monglife.mongs.domain.match.dto.etc.CreateMatchDto;
import com.monglife.mongs.domain.match.dto.etc.EnterMatchDto;
import com.monglife.mongs.domain.match.dto.etc.ExitMatchDto;
import com.monglife.mongs.domain.match.dto.etc.PickMatchDto;
import com.monglife.mongs.domain.match.entity.MatchPlayerEntity;
import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import com.monglife.mongs.domain.match.entity.MatchRoundEntity;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import com.monglife.mongs.domain.match.exception.*;
import com.monglife.mongs.domain.match.repository.ComnCodeRepository;
import com.monglife.mongs.domain.match.repository.MatchRoomRepository;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import com.monglife.mongs.domain.match.vo.CreateMatchVo;
import com.monglife.mongs.domain.match.vo.FightMatchVo;
import com.monglife.mongs.domain.match.vo.OverMatchVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private static final Random random = new Random();

    @Value("${application.service.match.bot-mong-type-group-code}")
    private String BOT_MONG_TYPE_GROUP_CODE;

    @Value("${application.service.match.max-round}")
    public Integer MAX_ROUND;

    @Value("${application.service.match.max-random-origin}")
    private Double MAX_RANDOM_ORIGIN;

    @Value("${application.service.match.max-random-bound}")
    private Double MAX_RANDOM_BOUND;

    private final MatchRoomRepository matchRoomRepository;

    private final ComnCodeRepository comnCodeRepository;


    /**
     * 배틀 생성
     * @param createMatchVoSet 매칭 플레이어 Set
     */
    @Transactional
    public CreateMatchDto createMatch(Set<CreateMatchVo> createMatchVoSet) {

        // 배틀 룸 엔티티 생성
        MatchRoomEntity matchRoomEntity = MatchRoomEntity.builder()
                .maxRound(MAX_ROUND)
                .build();

        // 배틀 플레이어 엔티티 생성
        List<MatchPlayerEntity> battlePlayerEntities = createMatchVoSet.stream()
                .map(createMatchVo -> {
                    double attackValue;
                    double healValue;
                    double defenceValue;
                    String mongTypeCode;

                    if (!createMatchVo.getIsBot()) {
                        // 봇이 아닌 경우
                        attackValue = MatchPlayerEntity.DEFAULT_ATTACK_VALUE + createMatchVo.getStrength();
                        healValue = MatchPlayerEntity.DEFAULT_HEAL_VALUE + createMatchVo.getFatigue();
                        defenceValue = MatchPlayerEntity.DEFAULT_DEFENCE_VALUE + createMatchVo.getWeight();
                        mongTypeCode = createMatchVo.getMongTypeCode();
                    } else {
                        // 봇인 경우
                        attackValue = MatchPlayerEntity.DEFAULT_ATTACK_VALUE;
                        healValue = MatchPlayerEntity.DEFAULT_HEAL_VALUE;
                        defenceValue = MatchPlayerEntity.DEFAULT_DEFENCE_VALUE;
                        mongTypeCode = comnCodeRepository.findByGroupCode(BOT_MONG_TYPE_GROUP_CODE).stream()
                                .findAny()
                                .orElseThrow(NotExistsMongTypeCodeException::new)
                                .getComnCode();
                    }

                    return MatchPlayerEntity.builder()
                            .playerId(createMatchVo.getPlayerId())
                            .deviceId(createMatchVo.getDeviceId())
                            .accountId(createMatchVo.getAccountId())
                            .mongId(createMatchVo.getMongId())
                            .mongTypeCode(mongTypeCode)
                            .defenceValue(defenceValue * random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .attackValue(attackValue * random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .healValue(healValue * random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .isBot(createMatchVo.getIsBot())
                            .build();
                })
                .toList();

        matchRoomEntity.joinBattlePlayer(battlePlayerEntities);

        // 실제 플레이어 필터링
        Set<String> playerIdSet = createMatchVoSet.stream()
                .filter(battlePlayerEntity -> !battlePlayerEntity.getIsBot())
                .map(CreateMatchVo::getPlayerId)
                .collect(Collectors.toSet());

        // 봇인 경우 MAX_ROUND 만큼의 round 미리 생성
        battlePlayerEntities.forEach(matchPlayerEntity -> {
            // 봇인 경우
            if (matchPlayerEntity.getIsBot()) {
                List<MatchRoundEntity> battleRoundEntities = new ArrayList<>();

                for (int round = 1; round <= matchRoomEntity.getMaxRound(); round++) {

                    // 선택 코드
                    List<MatchRoundCode> matchRoundCodes
                            = List.of(MatchRoundCode.MATCH_PICK_ATTACK, MatchRoundCode.MATCH_PICK_DEFENCE, MatchRoundCode.MATCH_PICK_HEAL);

                    int battleRoundCodeIndex = random.nextInt(matchRoundCodes.size());
                    MatchRoundCode botRoundCode = matchRoundCodes.get(battleRoundCodeIndex);

                    // 봇 playerId
                    String playerId = matchPlayerEntity.getPlayerId();

                    // 상대 playerId
                    int targetPlayerIdIndex = random.nextInt(playerIdSet.size());
                    String targetPlayerId = playerIdSet.stream()
                            .skip(targetPlayerIdIndex)
                            .findFirst()
                            .orElseThrow(() -> new OnlyBotMatchingException(playerIdSet));

                    MatchRoundEntity matchRoundEntity = switch (botRoundCode) {
                        case MATCH_PICK_DEFENCE -> MatchRoundEntity.builder()
                                    .playerId(matchPlayerEntity.getPlayerId())
                                    .targetPlayerId(playerId)
                                    .round(round)
                                    .roundCode(botRoundCode)
                                    .roundValue(matchPlayerEntity.getDefenceValue())
                                    .build();
                        case MATCH_PICK_HEAL -> MatchRoundEntity.builder()
                                    .playerId(matchPlayerEntity.getPlayerId())
                                    .targetPlayerId(playerId)
                                    .round(round)
                                    .roundCode(botRoundCode)
                                    .roundValue(matchPlayerEntity.getHealValue())
                                    .build();
                        default -> MatchRoundEntity.builder()
                                    .playerId(matchPlayerEntity.getPlayerId())
                                    .targetPlayerId(targetPlayerId)
                                    .round(round)
                                    .roundCode(botRoundCode)
                                    .roundValue(matchPlayerEntity.getAttackValue())
                                    .build();
                    };

                    battleRoundEntities.add(matchRoundEntity);
                }

                matchRoomEntity.joinBattleRound(battleRoundEntities);
            }
        });

        matchRoomRepository.save(matchRoomEntity);

        Set<MatchPlayerVo> battlePlayers = battlePlayerEntities.stream()
                .map(matchPlayerEntity -> MatchPlayerVo.of(matchPlayerEntity, MatchRoundCode.NONE))
                .collect(Collectors.toSet());

        return CreateMatchDto.builder()
                .roomId(matchRoomEntity.getRoomId())
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
    public EnterMatchDto enterMatch(Long roomId, String playerId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveFalseAndRound(roomId, 0)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        matchRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 입장 처리
        matchRoomEntity.enterBattlePlayer(playerId);

        FightMatchVo fightMatchVo = null;
        Boolean isEnterAll = matchRoomEntity.isMatchPlayerEnterAll();

        if (isEnterAll) {
            Set<MatchPlayerVo> battlePlayers = matchRoomEntity.getMatchPlayerSet().stream()
                    .map(matchPlayerEntity -> MatchPlayerVo.of(matchPlayerEntity, MatchRoundCode.NONE))
                    .collect(Collectors.toSet());

            Integer round = matchRoomEntity.getRound();
            Boolean isLastRound = matchRoomEntity.isLastRound();

            fightMatchVo = FightMatchVo.builder()
                    .round(round)
                    .matchPlayers(battlePlayers)
                    .isLastRound(isLastRound)
                    .build();

            matchRoomEntity.start();
        }

        return EnterMatchDto.builder()
                .isEnterAll(isEnterAll)
                .fightMatchVo(fightMatchVo)
                .build();
    }

    /**
     * 배틀 퇴장
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     */
    @Transactional
    public ExitMatchDto exitBattle(Long roomId, String playerId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        matchRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 퇴장 처리
        matchRoomEntity.excludeBattlePlayer(playerId);

        List<OverMatchVo> overMatchVos = null;
        Boolean isExitAll = matchRoomEntity.isBattlePlayerExitAll();

        if (isExitAll) {
            Set<MatchPlayerEntity> battlePlayerEntities =  matchRoomEntity.getMatchPlayerSet();

            List<MatchPlayerEntity> rankBattlePlayerEntities = rankBattlePlayer(battlePlayerEntities);

            overMatchVos = rankBattlePlayerEntities.stream()
                    .map(matchPlayerEntity -> OverMatchVo.builder()
                                .playerId(matchPlayerEntity.getPlayerId())
                                .mongId(matchPlayerEntity.getMongId())
                                .mongTypeCode(matchPlayerEntity.getMongTypeCode())
                                .build())
                    .toList();
        } else {
            Integer round = matchRoomEntity.getRound();

            // 현재 라운드 선택한 경우
            if (matchRoomEntity.getCurrentBattleRound(playerId).isPresent()) {
                round = round + 1;
            }

            round = Math.min(round, matchRoomEntity.getMaxRound());

            while (round <= matchRoomEntity.getMaxRound()) {

                MatchRoundEntity matchRoundEntity = MatchRoundEntity.builder()
                        .playerId(playerId)
                        .targetPlayerId(playerId)
                        .round(round)
                        .roundCode(MatchRoundCode.NONE)
                        .roundValue(0D)
                        .build();

                matchRoomEntity.joinBattleRound(matchRoundEntity);

                round++;
            }
        }

        return ExitMatchDto.builder()
                .isExitAll(isExitAll)
                .overMatchVos(overMatchVos)
                .build();
    }

    /**
     * 배틀 선택
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     * @return  모든 플레이어 선택 완료 여부
     */
    @Transactional
    public PickMatchDto pickBattle(Long roomId, String playerId, String targetPlayerId, MatchRoundCode matchRoundCode) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Integer round = matchRoomEntity.getRound();

        // 선택 여부 확인
        matchRoomEntity.getCurrentBattleRound(playerId)
                .ifPresent(matchRoundEntity -> {
                    throw new AlreadyExistsRoundException(round, playerId);
                });

        // 현재 플레이어
        MatchPlayerEntity matchPlayerEntity = matchRoomEntity.getBattlePlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 타켓 플레이어 존재 확인
        matchRoomEntity.getBattlePlayer(targetPlayerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(targetPlayerId));

        MatchRoundEntity matchRoundEntity = switch (matchRoundCode) {
            case MATCH_PICK_DEFENCE -> MatchRoundEntity.builder()
                    .playerId(playerId)
                    .targetPlayerId(targetPlayerId)
                    .round(round)
                    .roundCode(matchRoundCode)
                    .roundValue(matchPlayerEntity.getDefenceValue())
                    .build();
            case MATCH_PICK_HEAL -> MatchRoundEntity.builder()
                    .playerId(playerId)
                    .targetPlayerId(targetPlayerId)
                    .round(round)
                    .roundCode(matchRoundCode)
                    .roundValue(matchPlayerEntity.getHealValue())
                    .build();
            default -> MatchRoundEntity.builder()
                    .playerId(playerId)
                    .targetPlayerId(targetPlayerId)
                    .round(round)
                    .roundCode(matchRoundCode)
                    .roundValue(matchPlayerEntity.getAttackValue())
                    .build();
        };

        // 라운드 등록
        matchRoomEntity.joinBattleRound(matchRoundEntity);

        Boolean isPickAll = matchRoomEntity.isBattleRoundPickAll();
        FightMatchVo fightMatchVo = null;

        // 전체 선택 여부 확인
        if (isPickAll) {

            Set<MatchPlayerVo> battlePlayers = matchRoomEntity.nextRound();

            Boolean isLastRound = matchRoomEntity.isLastRound() || matchRoomEntity.isBattlePlayerDeadAll();

            fightMatchVo = FightMatchVo.builder()
                    .round(round)
                    .matchPlayers(battlePlayers)
                    .isLastRound(isLastRound)
                    .build();
        }

        return PickMatchDto.builder()
                .isPickAll(isPickAll)
                .fightMatchVo(fightMatchVo)
                .build();
    }

    /**
     * 배틀 종료
     * @param roomId 배틀룸 ID
     */
    @Transactional
    public void overBattle(Long roomId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        matchRoomEntity.over();
    }

    /**
     * 종료 된 배틀 결과 조회
     * @param roomId 배틀룸 ID
     * @return 등수별 정렬한 OverBattleDto 목록
     */
    @Transactional
    public List<OverMatchVo> findOverBattle(Long roomId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveFalse(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Set<MatchPlayerEntity> battlePlayerEntities =  matchRoomEntity.getMatchPlayerSet();

        List<MatchPlayerEntity> rankBattlePlayerEntities = rankBattlePlayer(battlePlayerEntities);

        return rankBattlePlayerEntities.stream()
                .map(matchPlayerEntity -> OverMatchVo.builder()
                            .playerId(matchPlayerEntity.getPlayerId())
                            .mongId(matchPlayerEntity.getMongId())
                            .mongTypeCode(matchPlayerEntity.getMongTypeCode())
                            .build())
                .toList();
    }

    /**
     * 배틀 플레이어 랭킹
     * @param battlePlayerEntities 배틀 플레이어 엔티티 목록
     * @return 배틀 플레이어 엔티티 등수 기준 정렬 리스트
     */
    private static List<MatchPlayerEntity> rankBattlePlayer(Set<MatchPlayerEntity> battlePlayerEntities) {

        // 나간 배틀 플레이어
        List<MatchPlayerEntity> rankBattlePlayerEntities = battlePlayerEntities.stream()
                .filter(matchPlayerEntity -> !matchPlayerEntity.getIsEnter())
                .sorted((bp1, bp2) -> {
                    if (bp1.getHp().equals(bp2.getHp())) {
                        return bp1.getExitDt().compareTo(bp2.getExitDt());
                    }
                    return bp2.getHp().compareTo(bp1.getHp());
                })
                .collect(Collectors.toList());

        // 나가지 않은 배틀 플레이어
        battlePlayerEntities.stream()
                .filter(MatchPlayerEntity::getIsEnter)
                // 역순 정렬
                .sorted((bp1, bp2) -> {
                    if (bp1.getHp().equals(bp2.getHp())) {
                        return bp2.getEnterDt().compareTo(bp1.getEnterDt());
                    }
                    return bp1.getHp().compareTo(bp2.getHp());
                })
                // 순위 리스트 앞에서 부터 삽입 (하위 등수 부터 저장)
                .forEachOrdered(matchPlayerEntity -> rankBattlePlayerEntities.add(0, matchPlayerEntity));

        return rankBattlePlayerEntities;
    }
}
