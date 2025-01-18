package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
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

    @Getter
    @AllArgsConstructor
    public enum MongHistoryType {

        DELETE("삭제"),
        STROKE("쓰다듬기"),
        SLEEP("수면"),
        WAKEUP("기상"),
        POOP_CLEAN("기상"),
        FEED("기상"),
        EVOLUTION_READY("진화 대기"),
        EVOLUTION("진화"),
        GRADUATE_READY("졸업 대기"),
        GRADUATE("졸업"),
        DEAD("죽음"),
        INCREASE_PAY_POINT("페이포인트 증가"),
        TRAINING("훈련"),
        ;

        public final String name;
    }
}
