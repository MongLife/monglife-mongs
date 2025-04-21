package com.monglife.mongs.adapter.out.member.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.mongs.domain.model.CollectionMap;
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
@Table(name = "mongs_collection_map")
public class CollectionMapEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_map_id")
    private Long collectionMapId;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_map_code")
    private ComnCodeEntity mapType;

    @Transient
    private Boolean isIncluded = true;

    @Builder
    public CollectionMapEntity(Long accountId, ComnCodeEntity mapType) {
        this.accountId = accountId;
        this.mapType = mapType;
    }

    public void exclude() {
        this.isIncluded = false;
    }

    /**
     * 컬렉션 맵 엔티티 도메인 변환
     * @return 컬렉션 맵 도메인 객체
     */
    public CollectionMap toDomain() {
        return CollectionMap.builder()
                .collectionMapId(this.collectionMapId)
                .accountId(this.accountId)
                .mapTypeCode(this.mapType.getCode())
                .mapTypeName(this.mapType.getName())
                .isIncluded(this.isIncluded)
                .build();
    }
}
