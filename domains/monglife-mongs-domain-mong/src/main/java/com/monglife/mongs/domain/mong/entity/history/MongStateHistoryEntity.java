package com.monglife.mongs.domain.mong.entity.history;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStateHistoryCode;
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
    @Column(name = "mong_state_history_code")
    private MongStateHistoryCode mongStateHistoryCode;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "mong_state_code")
    private MongStateCode code;

    @Column(name = "is_sleep")
    private Boolean isSleep;

    @Builder
    public MongStateHistoryEntity(MongStateHistoryCode mongStateHistoryCode, MongStateCode code, Boolean isSleep) {
        this.mongStateHistoryCode = mongStateHistoryCode;
        this.code = code;
        this.isSleep = isSleep;
    }
}
