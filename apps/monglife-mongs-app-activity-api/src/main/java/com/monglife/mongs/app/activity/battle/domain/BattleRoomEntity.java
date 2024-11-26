package com.monglife.mongs.app.activity.battle.domain;

import com.monglife.mongs.app.activity.battle.enums.BattleRoundCode;
import com.monglife.mongs.app.activity.battle.vo.BattlePlayerVo;
import com.monglife.mongs.module.jpa.domain.BaseTimeEntity;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mongs_activity_battle_room")
public class BattleRoomEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "round")
    private Integer round = 0;

    @Column(name = "max_round")
    private Integer maxRound;

    @Column(name = "is_active")
    private Boolean isActive = Boolean.FALSE;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Set<BattlePlayerEntity> battlePlayerSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Set<BattleRoundEntity> battleRoundSet = new HashSet<>();


    @Builder
    public BattleRoomEntity(Integer maxRound) {
        this.maxRound = maxRound;
    }

    /**
     * 배틀 시작
     */
    public void start() {
        this.round = 1;
        this.isActive = Boolean.TRUE;
    }

    /**
     * 배틀 종료
     */
    public void over() {
        this.isActive = Boolean.FALSE;

        for (BattlePlayerEntity battlePlayerEntity : this.battlePlayerSet) {
            battlePlayerEntity.exit();
        }
    }

    public Boolean isLastRound() {

        boolean isLastRound = this.round.equals(this.maxRound + 1);

        if (isLastRound) {
            this.round = this.round - 1;
        }

        return isLastRound;
    }

    /**
     * 모든 배틀 플레이어 입장 여부 확인
     * @return 모든 배틀 플레이어 입장 여부
     */
    public Boolean isBattlePlayerEnterAll() {
        for (BattlePlayerEntity battlePlayerEntity : this.battlePlayerSet) {
            if (!battlePlayerEntity.getIsEnter()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 모든 배틀 플레이어 퇴장 여부 확인
     * @return 모든 배틀 플레이어가 나가거나 한명만 남았을 경우 true
     */
    public Boolean isBattlePlayerExitAll() {
        int battlePlayerCount = this.battlePlayerSet.size();
        for (BattlePlayerEntity battlePlayerEntity : this.battlePlayerSet) {
            if (!battlePlayerEntity.getIsEnter()) {
                battlePlayerCount--;
            }
        }
        return battlePlayerCount <= 1;
    }

    /**
     * 현재 라운드에서 플레이어의 선택 완료 여부
     * @return 현재 라운드에서 모든 플레이어의 선택 완료 여부
     */
    public Boolean isBattleRoundPickAll() {

        Set<String> battlePlayerIds = this.battlePlayerSet.stream()
                .map(BattlePlayerEntity::getPlayerId)
                .collect(Collectors.toSet());

        for (BattleRoundEntity battleRoundEntity : this.battleRoundSet) {
            if (this.round.equals(battleRoundEntity.getRound())) {
                battlePlayerIds.remove(battleRoundEntity.getPlayerId());
            }
        }

        return battlePlayerIds.isEmpty();
    }

    /**
     * 모든 플레이어 사망 여부 확인
     * @return 0 ~ 1명의 플레이어 생존 여부
     */
    public Boolean isBattlePlayerDeadAll() {
        int deadPlayerCount = this.battlePlayerSet.size();
        for (BattlePlayerEntity battlePlayerEntity : this.battlePlayerSet) {
            if (battlePlayerEntity.getHp() == 0) {
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
    public Optional<BattlePlayerEntity> getBattlePlayer(String playerId) {
        for (BattlePlayerEntity battlePlayerEntity : this.battlePlayerSet) {
            if (battlePlayerEntity.getPlayerId().equals(playerId)) {
                return Optional.of(battlePlayerEntity);
            }
        }
        return Optional.empty();
    }

    /**
     * 현재 라운드에 해당하는 플레이어의 라운드 엔티티 반환
     * @param playerId 플레이어 ID
     * @return 플레이 라운드 엔티티 Optional 객체
     */
    public Optional<BattleRoundEntity> getCurrentBattleRound(String playerId) {
        for (BattleRoundEntity battleRoundEntity : this.battleRoundSet) {
            if (battleRoundEntity.getPlayerId().equals(playerId) && battleRoundEntity.getRound().equals(this.round)) {
                return Optional.of(battleRoundEntity);
            }
        }
        return Optional.empty();
    }

    /**
     * 플레이어 추가
     * @param battlePlayerEntities 입장할 플레이어 엔티티
     */
    public void joinBattlePlayer(List<BattlePlayerEntity> battlePlayerEntities) {
        this.battlePlayerSet.addAll(battlePlayerEntities);
    }

    /**
     * 플레이어 입장
     * @param playerId 플레이어 ID
     */
    public void enterBattlePlayer(String playerId) {
        for (BattlePlayerEntity battlePlayerEntity : this.battlePlayerSet) {
            if (battlePlayerEntity.getPlayerId().equals(playerId)) {
                battlePlayerEntity.enter();
                break;
            }
        }
    }

    /**
     * 플레이어 중도 퇴장
     * @param playerId 퇴장할 플레이어 ID
     */
    public void excludeBattlePlayer(String playerId) {
        for (BattlePlayerEntity battlePlayerEntity : this.battlePlayerSet) {
            if (battlePlayerEntity.getPlayerId().equals(playerId)) {
                battlePlayerEntity.duringRoundExit();
                break;
            }
        }
    }

    /**
     * 라운드 단건 등록
     * @param battleRoundEntity 라운드 엔티티
     */
    public void joinBattleRound(BattleRoundEntity battleRoundEntity) {
        this.battleRoundSet.add(battleRoundEntity);
    }

    /**
     * 라운드 다건 등록
     * @param battleRoundEntities 라운드 엔티티 목록
     */
    public void joinBattleRound(List<BattleRoundEntity> battleRoundEntities) {
        this.battleRoundSet.addAll(battleRoundEntities);
    }

    /**
     * 다음 라운드로 진행
     */
    public Set<BattlePlayerVo> nextRound() {

        Map<String, BattlePlayerEntity> battlePlayerEntityMap = this.battlePlayerSet.stream()
                .collect(Collectors.toMap(BattlePlayerEntity::getPlayerId, battlePlayerEntity -> battlePlayerEntity));

        this.battleRoundSet.stream()
                .filter(battleRoundEntity -> battleRoundEntity.getRound().equals(this.round))
                .forEach(battleRoundEntity -> {

                    String targetPlayerId = battleRoundEntity.getTargetPlayerId();
                    Double roundValue = battleRoundEntity.getRoundValue();
                    BattleRoundCode roundCode = battleRoundEntity.getRoundCode();

                    BattlePlayerEntity targetBattlePlayerEntity = battlePlayerEntityMap.get(targetPlayerId);

                    switch (roundCode) {
                        case BATTLE_PICK_ATTACK -> targetBattlePlayerEntity.attacked(roundValue);
                        case BATTLE_PICK_HEAL -> targetBattlePlayerEntity.healed(roundValue);
                        case BATTLE_PICK_DEFENCE -> targetBattlePlayerEntity.defenced(roundValue);
                    }
                });

        this.round = this.round + 1;

        return this.battlePlayerSet.stream()
                .map(battlePlayerEntity -> BattlePlayerVo.of(battlePlayerEntity, battlePlayerEntity.hpCalculation()))
                .collect(Collectors.toSet());
    }
}
