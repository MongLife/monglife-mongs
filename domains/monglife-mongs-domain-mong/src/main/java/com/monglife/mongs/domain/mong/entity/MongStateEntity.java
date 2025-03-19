package com.monglife.mongs.domain.mong.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStateHistoryType;
import com.monglife.mongs.domain.mong.listener.MongStateEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners({ AuditingEntityListener.class, MongStateEntityListener.class })
@Table(name = "mongs_mong_state")
@ToString(exclude = { "history", "mong" })
public class MongStateEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_state_id")
    private Long mongStateId;

    @OneToOne(mappedBy = "state", cascade = CascadeType.PERSIST)
    private MongEntity mong;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_state_code")
    private MongStateCode code;

    @Column(name = "is_sleep")
    private Boolean isSleep;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mong_state_id")
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

        this.addHistory(MongStateHistoryType.SET_SLEEP);
    }

    /**
     * 기상 상태로 변경
     */
    public void setWakeup() {

        if (!this.isSleep) return;

        this.isSleep = Boolean.FALSE;

        this.addHistory(MongStateHistoryType.SET_WAKEUP);
    }

    /**
     * state code 설정
     * @param code state code
     */
    public void setCode(MongStateCode code) {

        if (this.code == code) return;

        this.code = code;

        this.addHistory(MongStateHistoryType.SET_CODE);
    }

    private void addHistory(MongStateHistoryType mongStateHistoryType) {

        this.history.add(MongStateHistoryEntity.builder()
                .mongId(this.mong.getMongId())
                .accountId(this.mong.getAccountId())
                .mongName(this.mong.getMongName())
                .mongStateHistoryType(mongStateHistoryType)
                .code(this.code)
                .isSleep(this.isSleep)
                .build());
    }
}
