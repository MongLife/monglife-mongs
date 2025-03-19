package com.monglife.mongs.domain.mong.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.mong.enums.MongHistoryType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_mong_history")
public class MongHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_history_id")
    private Long mongHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_history_type")
    private MongHistoryType mongHistoryType;

    @Column(name = "pay_point")
    protected Integer payPoint;

    @Column(name = "mong_type_code")
    private String typeCode;


    @Builder
    public MongHistoryEntity(MongHistoryType mongHistoryType, Integer payPoint, String typeCode) {
        this.mongHistoryType = mongHistoryType;
        this.payPoint = payPoint;
        this.typeCode = typeCode;
    }
}
