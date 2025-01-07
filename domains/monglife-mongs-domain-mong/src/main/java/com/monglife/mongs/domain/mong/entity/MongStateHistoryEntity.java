package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mongs_mong_state_history")
public class MongStateHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_state_history_id")
    private Long mongStateHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_state_history_type")
    private MongStateHistoryType mongStateHistoryType;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "mong_state_code")
    private MongStateCode code;

    @Column(name = "is_sleep")
    private Boolean isSleep;

    @Builder
    public MongStateHistoryEntity(MongStateHistoryType mongStateHistoryType, MongStateCode code, Boolean isSleep) {
        this.mongStateHistoryType = mongStateHistoryType;
        this.code = code;
        this.isSleep = isSleep;
    }

    @Getter
    @AllArgsConstructor
    public enum MongStateHistoryType {

        HISTORY_MONG_STATE_SET_SLEEP("수면 상태로 변경"),
        HISTORY_MONG_STATE_SET_WAKEUP("수면 상태로 변경"),
        HISTORY_MONG_STATE_SET_CODE("상태 코드 변경"),
        ;

        public final String name;
    }
}
