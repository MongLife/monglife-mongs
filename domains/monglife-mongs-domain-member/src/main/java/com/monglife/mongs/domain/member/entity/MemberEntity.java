package com.monglife.mongs.domain.member.entity;

import com.monglife.mongs.domain.member.listener.MemberEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class, MemberEntityListener.class })
@Table(name = "mongs_member")
public class MemberEntity extends BaseTimeEntity {

    private static final Integer MAX_SLOT = 3;

    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "slot_count")
    private Integer slotCount;

    @Column(name = "start_point")
    private Integer starPoint;

    @Column(name = "walking_count")
    private Integer walkingCount;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<CollectionMapEntity> collectionMaps;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<CollectionMongEntity> collectionMongs;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<ProductOrderEntity> payments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<FeedbackEntity> feedbacks;

    @Builder
    public MemberEntity(Long accountId) {
        this.accountId = accountId;
        this.slotCount = 1;
        this.starPoint = 0;
        this.walkingCount = 0;
        this.isActive = Boolean.TRUE;
        this.collectionMaps = new ArrayList<>();
        this.collectionMongs = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
    }

    public void joinCollectionMap(CollectionMapEntity collectionMapEntity) {
        this.collectionMaps.add(collectionMapEntity);
    }

    public void joinCollectionMong(CollectionMongEntity collectionMongEntity) {
        this.collectionMongs.add(collectionMongEntity);
    }

    public void joinProductOrder(ProductOrderEntity productOrderEntity) {
        this.payments.add(productOrderEntity);
    }

    public void joinFeedback(FeedbackEntity feedbackEntity) {
        this.feedbacks.add(feedbackEntity);
    }

    public void increaseSlotCount() {
        this.slotCount = Math.max(this.slotCount + 1, MAX_SLOT);
    }

    public void decreaseSlotCount() {
        this.slotCount = Math.max(1, this.slotCount - 1);
    }

    public void increaseStarPoint(Integer addStarPoint) {
        this.starPoint = this.starPoint + addStarPoint;
    }

    public void decreaseStarPoint(Integer subStarPoint) {
        this.starPoint = Math.max(0, this.starPoint - subStarPoint);
    }

    public void increaseWalkingCount(Integer addWalkingCount) {
        this.walkingCount = Math.min(this.walkingCount + addWalkingCount, Integer.MAX_VALUE);
    }

    public void decreaseWalkingCount(Integer subWalkingCount) {
        this.walkingCount = Math.max(0, this.walkingCount - subWalkingCount);
    }
}
