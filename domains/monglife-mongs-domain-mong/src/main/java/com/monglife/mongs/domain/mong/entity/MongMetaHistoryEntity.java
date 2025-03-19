package com.monglife.mongs.domain.mong.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.mong.enums.MongMetaHistoryType;
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

    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "mong_name")
    private String mongName;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_meta_history_type")
    private MongMetaHistoryType mongMetaHistoryType;

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
    public MongMetaHistoryEntity(Long mongId, Long accountId, String mongName, MongMetaHistoryType mongMetaHistoryType, Integer trainingCount, Integer strokeCount, Double reward, Double penalty, Boolean isActive) {
        this.mongId = mongId;
        this.accountId = accountId;
        this.mongName = mongName;
        this.mongMetaHistoryType = mongMetaHistoryType;
        this.trainingCount = trainingCount;
        this.strokeCount = strokeCount;
        this.reward = reward;
        this.penalty = penalty;
        this.isActive = isActive;
    }
}
