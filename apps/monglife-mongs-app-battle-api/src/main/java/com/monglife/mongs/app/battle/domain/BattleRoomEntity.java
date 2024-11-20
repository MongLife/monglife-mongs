package com.monglife.mongs.app.battle.domain;

import com.monglife.mongs.module.jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "battle_room")
public class BattleRoomEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "round")
    private Integer round = 0;

    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Set<BattlePlayerEntity> battlePlayerSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Set<BattleRoundEntity> battleRoundSet = new HashSet<>();

    @Builder
    public BattleRoomEntity(Long roomId, Integer round, Boolean isActive, Set<BattlePlayerEntity> battlePlayerSet, Set<BattleRoundEntity> battleRoundSet) {
        this.roomId = roomId;
        this.round = round;
        this.isActive = isActive;
        this.battlePlayerSet = battlePlayerSet;
        this.battleRoundSet = battleRoundSet;
    }

    public void start() {
        this.isActive = Boolean.TRUE;
    }

    public void over() {
        this.isActive = Boolean.FALSE;
    }

    public Boolean isBattlePlayerEnterAll() {
        for (BattlePlayerEntity battlePlayerEntity : battlePlayerSet) {
            if (!battlePlayerEntity.getIsEnter()) {
                return false;
            }
        }
        return true;
    }

    public Boolean isBattlePlayerExitAll() {
        return this.battlePlayerSet.size() <= 1;
    }

    public Boolean isBattleRoundPickAll() {

        Set<String> battlePlayerIds = this.battlePlayerSet.stream()
                .map(BattlePlayerEntity::getPlayerId)
                .collect(Collectors.toSet());

        for (BattleRoundEntity battleRoundEntity : battleRoundSet) {
            battlePlayerIds.remove(battleRoundEntity.getPlayerId());
        }

        return !battlePlayerIds.isEmpty();
    }

    // 플레이어 엔티티 반환
    public Optional<BattlePlayerEntity> getBattlePlayer(String playerId) {
        for (BattlePlayerEntity battlePlayerEntity : battlePlayerSet) {
            if (battlePlayerEntity.getPlayerId().equals(playerId)) {
                return Optional.of(battlePlayerEntity);
            }
        }
        return Optional.empty();
    }

    // 현재 라운드에 해당하는 플레이어의 라운드 엔티티 반환
    public Optional<BattleRoundEntity> getCurrentBattleRound(String playerId) {
        for (BattleRoundEntity battleRoundEntity : battleRoundSet) {
            if (battleRoundEntity.getPlayerId().equals(playerId) && battleRoundEntity.getRound().equals(this.round)) {
                return Optional.of(battleRoundEntity);
            }
        }
        return Optional.empty();
    }

    // 플레이어 입장
    public void joinBattlePlayer(List<BattlePlayerEntity> battlePlayerEntities) {
        this.battlePlayerSet.addAll(battlePlayerEntities);
    }

    // 라운드 단건 등록
    public void joinBattleRound(BattleRoundEntity battleRoundEntity) {
        this.battleRoundSet.add(battleRoundEntity);
    }

    // 라운드 다건 등록
    public void joinBattleRound(List<BattleRoundEntity> battleRoundEntities) {
        this.battleRoundSet.addAll(battleRoundEntities);
    }

    public void increaseRound() {
        this.round = this.round + 1;
    }
}
