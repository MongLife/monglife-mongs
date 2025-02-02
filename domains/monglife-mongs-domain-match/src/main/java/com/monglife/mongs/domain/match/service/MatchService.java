package com.monglife.mongs.domain.match.service;

import com.monglife.mongs.domain.match.dto.etc.CreateMatchDto;
import com.monglife.mongs.domain.match.dto.etc.PickMatchDto;
import com.monglife.mongs.domain.match.dto.event.EnterMatchEvent;
import com.monglife.mongs.domain.match.dto.event.OverMatchEvent;
import com.monglife.mongs.domain.match.entity.MatchPlayerEntity;
import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import com.monglife.mongs.domain.match.entity.MatchRoundEntity;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import com.monglife.mongs.domain.match.exception.*;
import com.monglife.mongs.domain.match.repository.ComnCodeRepository;
import com.monglife.mongs.domain.match.repository.MatchRoomRepository;
import com.monglife.mongs.domain.match.utils.MatchUtil;
import com.monglife.mongs.domain.match.vo.CreateMatchVo;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import com.monglife.mongs.domain.match.vo.MatchVo;
import com.monglife.mongs.domain.match.vo.OverMatchVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private static final Random random = new Random();

    private final ApplicationEventPublisher applicationEventPublisher;

    // 봇 몽 그룹 코드
    @Value("${application.service.match.bot-mong-type-group-code}")
    private String BOT_MONG_TYPE_GROUP_CODE;

    // 매치 최대 라운드 수
    @Value("${application.service.match.max-round}")
    public Integer MAX_ROUND;

    // 매치 플레이어 공격, 방어, 힐 수치 랜덤 값 시작점
    @Value("${application.service.match.max-random-origin}")
    private Double MAX_RANDOM_ORIGIN;

    // 매치 플레이어 공격, 방어, 힐 수치 랜덤 값 종료점
    @Value("${application.service.match.max-random-bound}")
    private Double MAX_RANDOM_BOUND;

    private final MatchRoomRepository matchRoomRepository;

    private final ComnCodeRepository comnCodeRepository;

    @Transactional(readOnly = true)
    public MatchVo getMatch(Long roomId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Set<MatchPlayerEntity> matchPlayerEntities = matchRoomEntity.getMatchPlayerSet();

        return MatchVo.builder()

                .build();
    }

    /**
     * 종료 된 배틀 결과 조회
     * @param roomId 배틀룸 ID
     * @return 등수별 정렬한 OverMatchDto 목록
     */
    @Transactional
    public List<OverMatchVo> getOverMatch(Long roomId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveFalse(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Set<MatchPlayerEntity> matchPlayerEntities =  matchRoomEntity.getMatchPlayerSet();

        List<MatchPlayerEntity> rankMatchPlayerEntities = MatchUtil.rankMatchPlayer(matchPlayerEntities);

        return rankMatchPlayerEntities.stream()
                .map(OverMatchVo::of)
                .toList();
    }

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
        List<MatchPlayerEntity> matchPlayerEntities = createMatchVoSet.stream()
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
                        mongTypeCode = comnCodeRepository.findByCodeLike(BOT_MONG_TYPE_GROUP_CODE).stream()
                                .findAny()
                                .orElseThrow(NotExistsMongTypeCodeException::new)
                                .getCode();
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

        matchRoomEntity.joinMatchPlayer(matchPlayerEntities);

        // 실제 플레이어 필터링
        Set<String> playerIdSet = createMatchVoSet.stream()
                .filter(matchPlayerEntity -> !matchPlayerEntity.getIsBot())
                .map(CreateMatchVo::getPlayerId)
                .collect(Collectors.toSet());

        // 봇인 경우 MAX_ROUND 만큼의 round 미리 생성
        matchPlayerEntities.forEach(matchPlayerEntity -> {
            // 봇인 경우
            if (matchPlayerEntity.getIsBot()) {
                List<MatchRoundEntity> matchRoundEntities = new ArrayList<>();

                for (int round = 1; round <= matchRoomEntity.getMaxRound(); round++) {

                    // 선택 코드
                    List<MatchRoundCode> matchRoundCodes
                            = List.of(MatchRoundCode.MATCH_PICK_ATTACK, MatchRoundCode.MATCH_PICK_DEFENCE, MatchRoundCode.MATCH_PICK_HEAL);

                    int matchRoundCodeIndex = random.nextInt(matchRoundCodes.size());
                    MatchRoundCode botRoundCode = matchRoundCodes.get(matchRoundCodeIndex);

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

                    matchRoundEntities.add(matchRoundEntity);
                }

                matchRoomEntity.joinMatchRound(matchRoundEntities);
            }
        });

        matchRoomRepository.save(matchRoomEntity);

        Set<MatchPlayerVo> matchPlayers = matchPlayerEntities.stream()
                .map(matchPlayerEntity -> MatchPlayerVo.of(matchPlayerEntity, MatchRoundCode.NONE))
                .collect(Collectors.toSet());

        return CreateMatchDto.builder()
                .roomId(matchRoomEntity.getRoomId())
                .matchPlayers(matchPlayers)
                .build();
    }

    /**
     * 배틀 입장
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     */
    @Transactional
    public void enterMatch(Long roomId, String playerId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveFalseAndRound(roomId, 0)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        matchRoomEntity.getMatchPlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 입장 처리
        matchRoomEntity.enterMatchPlayer(playerId);

        if (matchRoomEntity.isPlayerEnterAll()) {
            // 입장 완료
            applicationEventPublisher.publishEvent(EnterMatchEvent.of(matchRoomEntity));
            // 매치 시작
            matchRoomEntity.start();
        }
    }

    /**
     * 배틀 퇴장
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     */
    @Transactional
    public void exitMatch(Long roomId, String playerId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        matchRoomEntity.getMatchPlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 퇴장 처리
        matchRoomEntity.excludeMatchPlayer(playerId);

        // 모든 플레이어가 퇴장하지 않은 상태
        if (matchRoomEntity.isPlayerExitAll()) {

            matchRoomEntity.over();

            // 퇴장 완료
            Set<MatchPlayerEntity> matchPlayerEntities =  matchRoomEntity.getMatchPlayerSet();

            List<MatchPlayerEntity> rankMatchPlayerEntities = MatchUtil.rankMatchPlayer(matchPlayerEntities);

            rankMatchPlayerEntities.stream().findFirst()
                    .ifPresent(matchPlayerEntity ->
                            applicationEventPublisher.publishEvent(OverMatchEvent.of(matchRoomEntity, matchPlayerEntity)));

        } else {
            // 퇴장한 플레이어 라운드 생성
            Integer round = matchRoomEntity.getRound();

            // 현재 라운드 선택한 경우
            if (matchRoomEntity.getCurrentMatchRound(playerId).isPresent()) {
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

                matchRoomEntity.joinMatchRound(matchRoundEntity);

                round++;
            }
        }
    }

    /**
     * 배틀 선택
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     */
    @Transactional
    public void pickMatch(Long roomId, String playerId, String targetPlayerId, MatchRoundCode matchRoundCode) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Integer round = matchRoomEntity.getRound();

        // 선택 여부 확인
        matchRoomEntity.getCurrentMatchRound(playerId)
                .ifPresent(matchRoundEntity -> {
                    throw new AlreadyExistsRoundException(round, playerId);
                });

        // 현재 플레이어
        MatchPlayerEntity matchPlayerEntity = matchRoomEntity.getMatchPlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 타켓 플레이어 존재 확인
        matchRoomEntity.getMatchPlayer(targetPlayerId)
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
        matchRoomEntity.joinMatchRound(matchRoundEntity);

        Boolean isPickAll = matchRoomEntity.isRoundPickAll();
        MatchVo matchVo = null;

        // 전체 선택 여부 확인
        if (isPickAll) {

            Set<MatchPlayerVo> matchPlayers = matchRoomEntity.nextRound();

            Boolean isLastRound = matchRoomEntity.isLastRound() || matchRoomEntity.isPlayerDeadAll();

            matchVo = MatchVo.builder()
                    .round(round)
                    .matchPlayers(matchPlayers)
                    .isLastRound(isLastRound)
                    .build();
        }
    }

    /**
     * 배틀 종료
     * @param roomId 배틀룸 ID
     */
    @Transactional
    public void overMatch(Long roomId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        matchRoomEntity.over();
    }
}
