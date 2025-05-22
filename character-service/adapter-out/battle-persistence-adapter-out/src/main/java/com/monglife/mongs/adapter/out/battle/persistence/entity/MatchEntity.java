package com.monglife.mongs.adapter.out.battle.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_match")
@ToString
public class MatchEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "max_round")
    private Integer maxRound;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_id")
    private Set<MatchPlayerEntity> matchPlayers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_id")
    private Set<MatchPickEntity> matchPicks = new HashSet<>();

    @Column(name = "round")
    private Integer round;

    @Column(name = "state_code")
    private String stateCode;

    @Builder
    public MatchEntity(Long matchId, Integer maxRound, Set<MatchPlayerEntity> matchPlayers, Set<MatchPickEntity> matchPicks, Integer round, String stateCode) {
        this.matchId = matchId;
        this.maxRound = maxRound;
        this.matchPlayers = matchPlayers;
        this.matchPicks = matchPicks;
        this.round = round;
        this.stateCode = stateCode;
    }
}
