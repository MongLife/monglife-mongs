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

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<CollectionMapEntity> collectionMaps = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<CollectionMongEntity> collectionMongs = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<FeedbackEntity> feedbacks = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<MemberHistoryEntity> history = new ArrayList<>();

    @Builder
    public MemberEntity(Long accountId) {
        this.accountId = accountId;
        this.slotCount = 1;
        this.starPoint = 0;
        this.isActive = Boolean.TRUE;
    }

    /**
     * 맵 컬렉션 등록
     * @param collectionMapEntity 맵 컬렉션 엔티티
     */
    public void joinCollectionMap(CollectionMapEntity collectionMapEntity) {

        this.collectionMaps.add(collectionMapEntity);

        this.addHistory(MemberHistoryEntity.MemberHistoryType.JOIN_COLLECTION_MAP);
    }

    /**
     * 몽 컬렉션 등록
     * @param collectionMongEntity 몽 컬렉션 엔티티
     */
    public void joinCollectionMong(CollectionMongEntity collectionMongEntity) {

        this.collectionMongs.add(collectionMongEntity);

        this.addHistory(MemberHistoryEntity.MemberHistoryType.JOIN_COLLECTION_MONG);
    }

    /**
     * 오류 신고 등록
     * @param feedbackEntity 오류 신고 엔티티
     */
    public void joinFeedback(FeedbackEntity feedbackEntity) {

        this.feedbacks.add(feedbackEntity);

        this.addHistory(MemberHistoryEntity.MemberHistoryType.JOIN_FEEDBACK);
    }

    /**
     * 보유 슬롯 증가
     */
    public void increaseSlotCount() {

        this.slotCount = Math.min(this.slotCount + 1, MAX_SLOT);

        this.addHistory(MemberHistoryEntity.MemberHistoryType.INCREASE_SLOT_COUNT);
    }

    /**
     * 보유 슬롯 감소
     */
    public void decreaseSlotCount() {

        this.slotCount = Math.max(1, this.slotCount - 1);

        this.addHistory(MemberHistoryEntity.MemberHistoryType.DECREASE_SLOT_COUNT);
    }

    /**
     * 스타 포인트 증가
     * @param starPoint 증가할 스타 포인트
     */
    public void increaseStarPoint(Integer starPoint) {

        this.starPoint = this.starPoint + starPoint;

        this.addHistory(MemberHistoryEntity.MemberHistoryType.INCREASE_STAR_POINT);
    }

    /**
     * 스타 포인트 감소
     * @param starPoint 감소할 스타 포인트
     */
    public void decreaseStarPoint(Integer starPoint) {

        this.starPoint = Math.max(0, this.starPoint - starPoint);

        this.addHistory(MemberHistoryEntity.MemberHistoryType.DECREASE_STAR_POINT);
    }

    private void addHistory(MemberHistoryEntity.MemberHistoryType memberHistoryType) {
        this.history.add(MemberHistoryEntity.builder()
                .memberHistoryType(memberHistoryType)
                .slotCount(this.slotCount)
                .starPoint(this.starPoint)
                .isActive(this.isActive)
                .build());
    }
}
