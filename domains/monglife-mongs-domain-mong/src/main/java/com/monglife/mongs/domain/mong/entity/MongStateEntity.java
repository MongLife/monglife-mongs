package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.listener.MongStateEntityListener;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Entity
@Getter
@EntityListeners(MongStateEntityListener.class)
@Table(name = "mongs_mong_state")
public class MongStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_state_id")
    private Long mongStateId;

    @OneToOne(mappedBy = "state", fetch = FetchType.LAZY)
    @JoinColumn(name = "mong_state_id")
    private MongEntity mong;

    @Enumerated(EnumType.STRING)
    @Column(name = "mong_state_code")
    private MongStateCode code;

    @Column(name = "reward")
    private Double reward;

    @Column(name = "penalty")
    private Double penalty;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_sleep")
    private Boolean isSleep;

    @Column(name = "is_time_limit")
    private Boolean isTimeLimit;

    public MongStateEntity() {
        this.code = MongStateCode.NORMAL;
        this.reward = 0D;
        this.penalty = 0D;
        this.isActive = Boolean.TRUE;
        this.isSleep = Boolean.FALSE;
        this.isTimeLimit = Boolean.FALSE;
    }

    public void update(UpdateDto updateDto) {
        this.code = Optional.ofNullable(updateDto.code).orElse(this.code);
        this.reward = Optional.ofNullable(updateDto.reward).orElse(this.reward);
        this.penalty = Optional.ofNullable(updateDto.penalty).orElse(this.penalty);
        this.isActive = Optional.ofNullable(updateDto.isActive).orElse(this.isActive);
        this.isSleep = Optional.ofNullable(updateDto.isSleep).orElse(this.isSleep);
        this.isTimeLimit = Optional.ofNullable(updateDto.isTimeLimit).orElse(isTimeLimit);
    }

    @Builder
    @AllArgsConstructor
    public static class UpdateDto {

        private MongStateCode code;

        private Double reward;

        private Double penalty;

        private Boolean isActive;

        private Boolean isSleep;

        private Boolean isTimeLimit;
    }
}
