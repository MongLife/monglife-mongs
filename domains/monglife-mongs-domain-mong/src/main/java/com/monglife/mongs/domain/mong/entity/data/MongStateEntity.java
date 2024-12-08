package com.monglife.mongs.domain.mong.entity.data;

import com.monglife.mongs.domain.mong.entity.history.MongStateHistoryEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStateHistoryCode;
import com.monglife.mongs.domain.mong.listener.MongStateEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Entity
@Getter
@EntityListeners(MongStateEntityListener.class)
@Table(name = "mongs_mong_state")
@ToString(exclude = "mong")
public class MongStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_state_id")
    private Long mongStateId;

    @OneToOne(mappedBy = "state")
    @JoinColumn(name = "mong_state_id")
    private MongEntity mong;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_state_code")
    private MongStateCode code;

    @Column(name = "is_sleep")
    private Boolean isSleep;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_state_id")
    private List<MongStateHistoryEntity> history;

    public MongStateEntity() {
        this.code = MongStateCode.NORMAL;
        this.isSleep = Boolean.FALSE;
    }

    public void setSleep() {
        this.isSleep = Boolean.TRUE;

        this.history.add(MongStateHistoryEntity.builder()
                .mongStateHistoryCode(MongStateHistoryCode.HISTORY_MONG_STATE_SET_SLEEP)
                .isSleep(this.isSleep)
                .build());
    }

    public void setWakeup() {
        this.isSleep = Boolean.FALSE;

        this.history.add(MongStateHistoryEntity.builder()
                .mongStateHistoryCode(MongStateHistoryCode.HISTORY_MONG_STATE_SET_WAKEUP)
                .isSleep(this.isSleep)
                .build());
    }

    public void setCode(MongStateCode code) {

        if (this.code == code) return;

        this.code = code;

        this.history.add(MongStateHistoryEntity.builder()
                .mongStateHistoryCode(MongStateHistoryCode.HISTORY_MONG_STATE_SET_CODE)
                .code(code)
                .build());
    }
}
