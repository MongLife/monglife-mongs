package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @Getter
    @AllArgsConstructor
    public enum MongHistoryType {

        HISTORY_MONG_DELETE("삭제"),
        HISTORY_MONG_STROKE("쓰다듬기"),
        HISTORY_MONG_SLEEP("수면"),
        HISTORY_MONG_WAKEUP("기상"),
        HISTORY_MONG_POOP_CLEAN("기상"),
        HISTORY_MONG_FEED("기상"),
        HISTORY_MONG_EVOLUTION_READY("진화 대기"),
        HISTORY_MONG_EVOLUTION("진화"),
        HISTORY_MONG_GRADUATE_READY("졸업 대기"),
        HISTORY_MONG_GRADUATE("졸업"),
        HISTORY_MONG_DEAD("죽음"),
        HISTORY_MONG_INCREASE_PAY_POINT("페이포인트 증가"),
        ;

        public final String name;
    }
}
