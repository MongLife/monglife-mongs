package com.monglife.mongs.domain.match.service;

import com.monglife.mongs.domain.match.dto.event.EnterMatchEvent;
import com.monglife.mongs.domain.match.dto.event.ExitMatchEvent;
import com.monglife.mongs.domain.match.dto.event.NextRoundEvent;
import com.monglife.mongs.domain.match.dto.event.OverMatchEvent;
import com.monglife.mongs.domain.match.entity.MatchPlayerEntity;
import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import com.monglife.mongs.domain.match.entity.MatchRoundEntity;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import com.monglife.mongs.domain.match.exception.*;
import com.monglife.mongs.domain.match.repository.ComnCodeRepository;
import com.monglife.mongs.domain.match.repository.MatchRoomRepository;
import com.monglife.mongs.domain.match.utils.MatchUtil;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import com.monglife.mongs.domain.match.vo.MatchVo;
import com.monglife.mongs.domain.match.vo.MatchingPlayerVo;
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

    /**
     * 매치 정보 조회
     * @param roomId 배틀룸 ID
     * @return 매치 정보 Vo
     */
    @Transactional(readOnly = true)
    public MatchVo getMatch(Long roomId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        return MatchVo.of(matchRoomEntity);
    }

    /**
     * 승리한 플레이어 조회
     * @param roomId 배틀룸 ID
     * @return 승리한 플레이어 정보 Vo
     */
    @Transactional
    public MatchPlayerVo getWinMatchPlayer(Long roomId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveFalse(roomId)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        Set<MatchPlayerEntity> matchPlayerEntities =  matchRoomEntity.getMatchPlayerSet();

        List<MatchPlayerEntity> rankMatchPlayerEntities = MatchUtil.rankMatchPlayer(matchPlayerEntities);

        MatchPlayerEntity matchPlayerEntity = rankMatchPlayerEntities.stream().findFirst()
                .orElseThrow(NotExistsPlayerException::new);

        return MatchPlayerVo.of(matchPlayerEntity);
    }

    /**
     * 배틀 생성
     * @param matchingPlayerVoSet 매칭 플레이어 Set
     */
    @Transactional
    public MatchVo createMatch(Set<MatchingPlayerVo> matchingPlayerVoSet) {

        // 배틀 룸 엔티티 생성
        MatchRoomEntity matchRoomEntity = MatchRoomEntity.builder()
                .maxRound(MAX_ROUND)
                .build();

        // 배틀 플레이어 엔티티 생성
        List<MatchPlayerEntity> matchPlayerEntities = matchingPlayerVoSet.stream()
                .map(matchingPlayerVo -> {
                    double attackValue;
                    double healValue;
                    double defenceValue;
                    String mongTypeCode;

                    if (!matchingPlayerVo.getIsBot()) {
                        // 봇이 아닌 경우
                        attackValue = MatchPlayerEntity.DEFAULT_ATTACK_VALUE + matchingPlayerVo.getStrength();
                        healValue = MatchPlayerEntity.DEFAULT_HEAL_VALUE + matchingPlayerVo.getFatigue();
                        defenceValue = MatchPlayerEntity.DEFAULT_DEFENCE_VALUE + matchingPlayerVo.getWeight();
                        mongTypeCode = matchingPlayerVo.getMongTypeCode();
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
                            .playerId(matchingPlayerVo.getPlayerId())
                            .deviceId(matchingPlayerVo.getDeviceId())
                            .accountId(matchingPlayerVo.getAccountId())
                            .mongId(matchingPlayerVo.getMongId())
                            .mongTypeCode(mongTypeCode)
                            .defenceValue(defenceValue * random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .attackValue(attackValue * random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .healValue(healValue * random.nextDouble(MAX_RANDOM_ORIGIN, MAX_RANDOM_BOUND))
                            .isBot(matchingPlayerVo.getIsBot())
                            .build();
                })
                .toList();

        matchRoomEntity.joinMatchPlayer(matchPlayerEntities);

        // 실제 플레이어 필터링
        Set<String> playerIdSet = matchingPlayerVoSet.stream()
                .filter(matchPlayerEntity -> !matchPlayerEntity.getIsBot())
                .map(MatchingPlayerVo::getPlayerId)
                .collect(Collectors.toSet());

        // 봇인 경우 MAX_ROUND 만큼의 round 미리 생성
        matchPlayerEntities.forEach(matchPlayerEntity -> {
            // 봇인 경우
            if (matchPlayerEntity.getIsBot()) {
                List<MatchRoundEntity> matchRoundEntities = new ArrayList<>();

                for (int round = matchRoomEntity.getPickRound(); round <= matchRoomEntity.getMaxRound(); round++) {

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

        return MatchVo.of(matchRoomEntity);
    }

    /**
     * 배틀 입장
     * @param roomId 배틀룸 ID
     * @param playerId 배틀 플레이어 ID
     */
    @Transactional
    public void enterMatch(Long roomId, String playerId) {

        MatchRoomEntity matchRoomEntity = matchRoomRepository.findByRoomIdAndIsActiveFalseAndRound(roomId, 1)
                .orElseThrow(() -> new NotExistsRoomIdException(roomId));

        MatchPlayerEntity matchPlayerEntity = matchRoomEntity.getMatchPlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 입장 처리
        matchPlayerEntity.enter();

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

        MatchPlayerEntity matchPlayerEntity = matchRoomEntity.getMatchPlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 퇴장 처리
        matchPlayerEntity.duringRoundExit();

        // 모든 플레이어 퇴장
        if (matchRoomEntity.isPlayerExitAll()) {

            matchRoomEntity.over();

            // 퇴장 완료
            List<MatchPlayerEntity> rankMatchPlayerEntities = MatchUtil.rankMatchPlayer(matchRoomEntity.getMatchPlayerSet());

            rankMatchPlayerEntities.stream().findFirst()
                    .ifPresent(winMatchPlayerEntity ->
                            applicationEventPublisher.publishEvent(ExitMatchEvent.of(matchRoomEntity.getRoomId(), winMatchPlayerEntity)));

        } else {
            // 퇴장한 플레이어 라운드 생성
            Integer round = matchRoomEntity.getPickRound();

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

        Integer round = matchRoomEntity.getPickRound();

        // 현재 플레이어
        MatchPlayerEntity matchPlayerEntity = matchRoomEntity.getMatchPlayer(playerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(playerId));

        // 타켓 플레이어 존재 확인
        MatchPlayerEntity targetMatchPlayerEntity = matchRoomEntity.getMatchPlayer(targetPlayerId)
                .orElseThrow(() -> new NotExistsPlayerIdException(targetPlayerId));

        MatchRoundEntity matchRoundEntity = switch (matchRoundCode) {
            case MATCH_PICK_DEFENCE -> MatchRoundEntity.builder()
                    .playerId(matchPlayerEntity.getPlayerId())
                    .targetPlayerId(targetMatchPlayerEntity.getPlayerId())
                    .round(round)
                    .roundCode(matchRoundCode)
                    .roundValue(matchPlayerEntity.getDefenceValue())
                    .build();
            case MATCH_PICK_HEAL -> MatchRoundEntity.builder()
                    .playerId(matchPlayerEntity.getPlayerId())
                    .targetPlayerId(targetMatchPlayerEntity.getPlayerId())
                    .round(round)
                    .roundCode(matchRoundCode)
                    .roundValue(matchPlayerEntity.getHealValue())
                    .build();
            default -> MatchRoundEntity.builder()
                    .playerId(matchPlayerEntity.getPlayerId())
                    .targetPlayerId(targetMatchPlayerEntity.getPlayerId())
                    .round(round)
                    .roundCode(matchRoundCode)
                    .roundValue(matchPlayerEntity.getAttackValue())
                    .build();
        };

        // 라운드 등록
        matchRoomEntity.joinMatchRound(matchRoundEntity);

        // 전체 선택 여부 확인
        if (matchRoomEntity.isRoundPickAll()) {
            matchRoomEntity.nextRound();

            if (matchRoomEntity.isLastRound()) {
                matchRoomEntity.over();

                MatchUtil.rankMatchPlayer(matchRoomEntity.getMatchPlayerSet()).stream().findFirst()
                        .ifPresent(winMatchPlayerEntity ->
                                applicationEventPublisher.publishEvent(OverMatchEvent.of(matchRoomEntity.getRoomId(), winMatchPlayerEntity)));
            }

            applicationEventPublisher.publishEvent(NextRoundEvent.of(matchRoomEntity));
        }
    }
}
