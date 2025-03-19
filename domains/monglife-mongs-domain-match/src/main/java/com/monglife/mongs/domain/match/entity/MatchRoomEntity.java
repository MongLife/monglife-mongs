package com.monglife.mongs.domain.match.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_match_room")
public class MatchRoomEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "round")
    private Integer round;

    @Column(name = "max_round")
    private Integer maxRound;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Set<MatchPlayerEntity> matchPlayerSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Set<MatchRoundEntity> matchRoundSet = new HashSet<>();

    @Builder
    public MatchRoomEntity(Integer maxRound) {
        this.round = 1;
        this.maxRound = maxRound;
        this.isActive = Boolean.FALSE;
    }

    public Integer getRound() {
        return this.round - 1;
    }

    public Integer getPickRound() {
        return this.round;
    }

    /**
     * 배틀 시작
     */
    public void start() {
        this.isActive = Boolean.TRUE;
    }

    /**
     * 배틀 종료
     */
    public void over() {

        this.isActive = Boolean.FALSE;

        for (MatchPlayerEntity matchPlayerEntity : this.matchPlayerSet) {
            matchPlayerEntity.exit();
        }
    }

    /**
     * 마지막 라운드 여부 확인
     * @return 마지막 라운드 여부
     */
    public Boolean isLastRound() {
        return this.round > this.maxRound || this.isPlayerDeadAll();
    }

    /**
     * 모든 배틀 플레이어 입장 여부 확인
     * @return 모든 배틀 플레이어 입장 여부
     */
    public Boolean isPlayerEnterAll() {
        for (MatchPlayerEntity matchPlayerEntity : this.matchPlayerSet) {
            if (!matchPlayerEntity.getIsEnter()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 모든 배틀 플레이어 퇴장 여부 확인
     * @return 모든 배틀 플레이어가 나가거나 한명만 남았을 경우 true
     */
    public Boolean isPlayerExitAll() {
        int matchPlayerCount = this.matchPlayerSet.size();
        for (MatchPlayerEntity matchPlayerEntity : this.matchPlayerSet) {
            if (!matchPlayerEntity.getIsEnter()) {
                matchPlayerCount--;
            }
        }
        return matchPlayerCount <= 1;
    }

    /**
     * 현재 라운드에서 플레이어의 선택 완료 여부
     * @return 현재 라운드에서 모든 플레이어의 선택 완료 여부
     */
    public Boolean isRoundPickAll() {

        Set<String> matchPlayerIds = this.matchPlayerSet.stream()
                .map(MatchPlayerEntity::getPlayerId)
                .collect(Collectors.toSet());

        for (MatchRoundEntity matchRoundEntity : this.matchRoundSet) {
            if (this.round.equals(matchRoundEntity.getRound())) {
                matchPlayerIds.remove(matchRoundEntity.getPlayerId());
            }
        }

        return matchPlayerIds.isEmpty();
    }

    /**
     * 모든 플레이어 사망 여부 확인
     * @return 0 ~ 1명의 플레이어 생존 여부
     */
    public Boolean isPlayerDeadAll() {
        int deadPlayerCount = this.matchPlayerSet.size();
        for (MatchPlayerEntity matchPlayerEntity : this.matchPlayerSet) {
            if (matchPlayerEntity.getHp() == 0) {
                deadPlayerCount--;
            }
        }
        return deadPlayerCount <= 1;
    }

    /**
     * 플레이어 엔티티 반환
     * @param playerId 플레이어 ID
     * @return 플레이어 엔티티 Optional 객체
     */
    public Optional<MatchPlayerEntity> getMatchPlayer(String playerId) {
        for (MatchPlayerEntity matchPlayerEntity : this.matchPlayerSet) {
            if (matchPlayerEntity.getPlayerId().equals(playerId)) {
                return Optional.of(matchPlayerEntity);
            }
        }
        return Optional.empty();
    }

    /**
     * 현재 라운드에 해당하는 플레이어의 라운드 엔티티 반환
     * @param playerId 플레이어 ID
     * @return 플레이 라운드 엔티티 Optional 객체
     */
    public Optional<MatchRoundEntity> getCurrentMatchRound(String playerId) {
        for (MatchRoundEntity matchRoundEntity : this.matchRoundSet) {
            if (matchRoundEntity.getPlayerId().equals(playerId) && this.round.equals(matchRoundEntity.getRound())) {
                return Optional.of(matchRoundEntity);
            }
        }
        return Optional.empty();
    }

    /**
     * 플레이어 추가
     * @param matchPlayerEntities 입장할 플레이어 엔티티
     */
    public void joinMatchPlayer(List<MatchPlayerEntity> matchPlayerEntities) {
        this.matchPlayerSet.addAll(matchPlayerEntities);
    }

    /**
     * 라운드 단건 등록
     * @param matchRoundEntity 라운드 엔티티
     */
    public void joinMatchRound(MatchRoundEntity matchRoundEntity) {

        this.getCurrentMatchRound(matchRoundEntity.getPlayerId())
                .ifPresent(existsMatchRoundEntity -> this.matchRoundSet.remove(existsMatchRoundEntity));

        this.matchRoundSet.add(matchRoundEntity);
    }

    /**
     * 라운드 다건 등록
     * @param matchRoundEntities 라운드 엔티티 목록
     */
    public void joinMatchRound(List<MatchRoundEntity> matchRoundEntities) {
        this.matchRoundSet.addAll(matchRoundEntities);
    }

    /**
     * 다음 라운드로 진행
     */
    public void nextRound() {

        Map<String, MatchPlayerEntity> matchPlayerEntityMap = this.matchPlayerSet.stream()
                .collect(Collectors.toMap(MatchPlayerEntity::getPlayerId, matchPlayerEntity -> matchPlayerEntity));

        this.matchRoundSet.stream()
                .filter(matchRoundEntity -> this.round.equals(matchRoundEntity.getRound()))
                .forEach(matchRoundEntity -> {

                    String targetPlayerId = matchRoundEntity.getTargetPlayerId();
                    Double roundValue = matchRoundEntity.getRoundValue();
                    MatchRoundCode roundCode = matchRoundEntity.getRoundCode();

                    MatchPlayerEntity targetMatchPlayerEntity = matchPlayerEntityMap.get(targetPlayerId);

                    switch (roundCode) {
                        case MATCH_PICK_ATTACK -> targetMatchPlayerEntity.attacked(roundValue);
                        case MATCH_PICK_HEAL -> targetMatchPlayerEntity.healed(roundValue);
                        case MATCH_PICK_DEFENCE -> targetMatchPlayerEntity.defenced(roundValue);
                    }
                });

        this.round = this.round + 1;

        this.matchPlayerSet.forEach(MatchPlayerEntity::next);
    }
}
