package com.monglife.mongs.adapter.out.member.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.model.Player;
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
@Table(name = "mongs_member")
public class MemberEntity extends BaseTimeEntity {

    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "slot_count")
    private Integer slotCount;

    @Column(name = "star_point")
    private Integer starPoint;

    @Column(name = "is_active")
    private Boolean isActive;

    @Builder
    public MemberEntity(Long accountId, Integer slotCount, Integer starPoint) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
        this.isActive = Boolean.TRUE;
    }

    /**
     * 플레이어 도메인을 기반으로 엔티티 수정
     * @param player 플레이어 도메인 객체
     */
    public void update(Player player) {
        this.slotCount = player.getSlotCount();
        this.starPoint = player.getStarPoint();
    }

    /**
     * 엔티티 도메인 변환
     * @return 플레이어 도메인 객체
     */
    public Player toDomain() {
        return Player.builder()
                .accountId(this.accountId)
                .slotCount(this.slotCount)
                .starPoint(this.starPoint)
                .build();
    }
}
