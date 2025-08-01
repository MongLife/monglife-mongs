package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.mongs.domain.mong.model.MongEvolutionHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_mong_evolution_history")
public class MongEvolutionHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_evolution_history_id")
    private Long mongEvolutionHistoryId;

    @Column(name = "mong_code")
    private String mongCode;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "evolution_score")
    private Double evolutionScore;

    @Builder
    public MongEvolutionHistoryEntity(Long mongEvolutionHistoryId, String mongCode, Long accountId, Double evolutionScore) {
        this.mongEvolutionHistoryId = mongEvolutionHistoryId;
        this.mongCode = mongCode;
        this.accountId = accountId;
        this.evolutionScore = evolutionScore;
    }

    public MongEvolutionHistory toDomain() {
        return MongEvolutionHistory.builder()
                .mongEvolutionHistoryId(mongEvolutionHistoryId)
                .mongCode(mongCode)
                .accountId(accountId)
                .evolutionScore(evolutionScore)
                .build();
    }
}
