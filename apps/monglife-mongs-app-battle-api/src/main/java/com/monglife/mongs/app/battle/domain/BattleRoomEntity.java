package com.monglife.mongs.app.battle.domain;

import com.monglife.mongs.module.jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "battle_room")
public class BattleRoomEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "round")
    private Integer round;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Set<BattlePlayerEntity> battlePlayerSet;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Set<BattleRoundEntity> battleRoundSet;

    @Builder
    public BattleRoomEntity() {
        this.round = 1;
        this.isActive = true;
        this.battlePlayerSet = new HashSet<>();
        this.battleRoundSet = new HashSet<>();
    }

    public void joinBattlePlayer(List<BattlePlayerEntity> battlePlayerEntities) {
        this.battlePlayerSet.addAll(battlePlayerEntities);
    }

    public void joinBattleRound(BattleRoundEntity battleRoundEntity) {
        this.battleRoundSet.add(battleRoundEntity);
    }

    public void joinBattleRound(List<BattleRoundEntity> battleRoundEntities) {
        this.battleRoundSet.addAll(battleRoundEntities);
    }
}
