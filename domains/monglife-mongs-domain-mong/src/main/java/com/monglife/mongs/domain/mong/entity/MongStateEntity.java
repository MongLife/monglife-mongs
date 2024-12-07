package com.monglife.mongs.domain.mong.entity;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.listener.MongStateEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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

    public MongStateEntity() {
        this.code = MongStateCode.NORMAL;
        this.isSleep = Boolean.FALSE;
    }

    public void update(UpdateDto updateDto) {
        this.code = Optional.ofNullable(updateDto.code).orElse(this.code);
        this.isSleep = Optional.ofNullable(updateDto.isSleep).orElse(this.isSleep);
    }

    @Builder
    @AllArgsConstructor
    public static class UpdateDto {

        private MongStateCode code;

        private Boolean isSleep;
    }
}
