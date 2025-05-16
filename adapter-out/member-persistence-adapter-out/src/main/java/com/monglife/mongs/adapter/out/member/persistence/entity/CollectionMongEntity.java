package com.monglife.mongs.adapter.out.member.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.member.model.CollectionMong;
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
@Table(name = "mongs_collection_mong")
public class CollectionMongEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_mong_id")
    private Long collectionMongId;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_mong_code")
    private ComnCodeEntity mongType;

    @Transient
    private Boolean isIncluded = true;

    @Builder
    public CollectionMongEntity(Long accountId, ComnCodeEntity mongType) {
        this.accountId = accountId;
        this.mongType = mongType;
    }

    public void exclude() {
        this.isIncluded = false;
    }

    /**
     * 컬렉션 몽 엔티티 도메인 변환
     * @return 컬렉션 몽 도메인 객체
     */
    public CollectionMong toDomain() {
        return CollectionMong.builder()
                .collectionMongId(this.collectionMongId)
                .accountId(this.accountId)
                .mongTypeCode(this.mongType.getCode())
                .mongTypeName(this.mongType.getName())
                .isIncluded(this.isIncluded)
                .build();
    }
}
