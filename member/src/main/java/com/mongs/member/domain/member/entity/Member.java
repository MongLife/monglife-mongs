package com.mongs.member.domain.member.entity;

import com.mongs.core.entity.BaseTimeEntity;
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
