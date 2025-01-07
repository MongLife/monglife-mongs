package com.monglife.mongs.domain.member.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_member_history")
public class MemberHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_history_id")
    private Long memberHistoryId;

    @Column(name = "slot_count")
    private Integer slotCount;

    @Column(name = "start_point")
    private Integer starPoint;

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_history_type")
    private MemberHistoryType memberHistoryType;

    @Builder
    public MemberHistoryEntity(Integer slotCount, Integer starPoint, Boolean isActive, MemberHistoryType memberHistoryType) {
        this.slotCount = slotCount;
        this.starPoint = starPoint;
        this.isActive = isActive;
        this.memberHistoryType = memberHistoryType;
    }

    @Getter
    @AllArgsConstructor
    public enum MemberHistoryType {

        JOIN_COLLECTION_MAP("컬렉션 맵 등록"),
        JOIN_COLLECTION_MONG("컬렉션 몽 등록"),
        JOIN_FEEDBACK("오류 신고 등록"),
        INCREASE_SLOT_COUNT("슬롯 수 증가"),
        DECREASE_SLOT_COUNT("슬롯 수 감소"),
        INCREASE_STAR_POINT("스타 포인트 증가"),
        DECREASE_STAR_POINT("스타 포인트 감소"),
        ;

        public final String name;
    }
}

