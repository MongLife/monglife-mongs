package com.monglife.mongs.domain.mong.entity.history;

import com.monglife.mongs.domain.mong.enums.MongMetaHistoryCode;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mongs_mong_meta_history")
public class MongMetaHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_meta_history_id")
    private Long mongMetaHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_meta_history_code")
    private MongMetaHistoryCode mongMetaHistoryCode;

    @Column(name = "training_count")
    private Integer trainingCount;

    @Column(name = "stroke_count")
    private Integer strokeCount;

    @Setter
    @Column(name = "reward")
    private Double reward;

    @Column(name = "penalty")
    private Double penalty;

    @Column(name = "is_active")
    private Boolean isActive;

    @Builder
    public MongMetaHistoryEntity(MongMetaHistoryCode mongMetaHistoryCode, Integer trainingCount, Integer strokeCount, Double reward, Double penalty, Boolean isActive) {
        this.mongMetaHistoryCode = mongMetaHistoryCode;
        this.trainingCount = trainingCount;
        this.strokeCount = strokeCount;
        this.reward = reward;
        this.penalty = penalty;
        this.isActive = isActive;
    }
}
