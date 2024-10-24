package com.mongs.play.domain.collection.entity;

import com.mongs.play.module.jpa.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Table(name = "map_collection")
public class MapCollection extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_collection_id")
    private Long id;
    @Column(updatable = false, nullable = false)
    private Long accountId;
    @Column(updatable = false, nullable = false)
    private String code;
}
