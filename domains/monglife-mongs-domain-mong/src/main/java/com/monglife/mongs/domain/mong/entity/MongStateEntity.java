package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@ToString(exclude = { "history" })
public class MongStateEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_state_code")
    private MongStateCode code;

    @Column(name = "is_sleep")
    private Boolean isSleep;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_id")
    private List<MongStateHistoryEntity> history = new ArrayList<>();

    public MongStateEntity() {
        this.code = MongStateCode.NORMAL;
        this.isSleep = Boolean.FALSE;
    }

    /**
     * 수면 상태로 변경
     */
    public void setSleep() {

        if (this.isSleep) return;

        this.isSleep = Boolean.TRUE;

        this.addHistory(MongStateHistoryEntity.MongStateHistoryType.HISTORY_MONG_STATE_SET_SLEEP);
    }

    /**
     * 기상 상태로 변경
     */
    public void setWakeup() {

        if (!this.isSleep) return;

        this.isSleep = Boolean.FALSE;

        this.addHistory(MongStateHistoryEntity.MongStateHistoryType.HISTORY_MONG_STATE_SET_WAKEUP);
    }

    /**
     * state code 설정
     * @param code state code
     */
    public void setCode(MongStateCode code) {

        if (this.code == code) return;

        this.code = code;

        this.addHistory(MongStateHistoryEntity.MongStateHistoryType.HISTORY_MONG_STATE_SET_CODE);
    }

    private void addHistory(MongStateHistoryEntity.MongStateHistoryType mongStateHistoryType) {

        this.history.add(MongStateHistoryEntity.builder()
                .mongStateHistoryType(mongStateHistoryType)
                .code(this.code)
                .isSleep(this.isSleep)
                .build());
    }
}
