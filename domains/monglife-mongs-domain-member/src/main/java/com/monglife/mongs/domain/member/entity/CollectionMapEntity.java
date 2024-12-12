package com.monglife.mongs.domain.member.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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
    private ComnCodeEntity comn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_map_position")
    private MapPositionEntity position;

    @Builder
    public CollectionMapEntity(Long collectionMapId, Long accountId, ComnCodeEntity comn, MapPositionEntity position) {
        this.collectionMapId = collectionMapId;
        this.accountId = accountId;
        this.comn = comn;
        this.position = position;
    }
}
