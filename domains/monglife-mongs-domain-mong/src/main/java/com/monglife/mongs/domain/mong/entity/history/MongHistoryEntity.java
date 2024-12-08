package com.monglife.mongs.domain.mong.entity.history;

import com.monglife.mongs.domain.mong.enums.MongHistoryCode;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
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
@Table(name = "mongs_mong_history")
public class MongHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_history_id")
    private Long mongHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_history_code")
    private MongHistoryCode mongHistoryCode;

    @Column(name = "pay_point")
    protected Integer payPoint;

    @Column(name = "mong_type_code")
    private String typeCode;


    @Builder
    public MongHistoryEntity(MongHistoryCode mongHistoryCode, Integer payPoint, String typeCode) {
        this.mongHistoryCode = mongHistoryCode;
        this.payPoint = payPoint;
        this.typeCode = typeCode;
    }
}
