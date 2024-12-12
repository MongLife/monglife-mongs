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
    private ComnCodeEntity comn;

    @Builder
    public CollectionMongEntity(Long accountId, ComnCodeEntity comn) {
        this.accountId = accountId;
        this.comn = comn;
    }
}
