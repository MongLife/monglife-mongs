package com.monglife.mongs.app.activity.training.domain;

import com.monglife.mongs.module.jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mongs_activity_training_history")
public class TrainingHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_history_id")
    private Long trainingHistoryId;

    @Column(name = "training_code")
    private String trainingCode;

    @Column(name = "reward_pay_point")
    private Integer rewardPayPoint;

    @Column(name = "is_rewarded")
    private Boolean isRewarded;
}
