package com.mongs.play.domain.member.entity;

import com.mongs.play.domain.core.jpa.baseEntity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
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
public class Member extends BaseTimeEntity {
    @Id
    private Long accountId;
    @Column(nullable = false)
    @Builder.Default
    private Integer maxSlot = 1;
    @Column(nullable = false)
    @Builder.Default
    private Integer starPoint = 0;
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
}
