package com.monglife.mongs.adapter.out.mong.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_mong_state")
@ToString(exclude = { "mong" })
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

    @Builder
    public MongStateEntity(Long mongStateId, MongEntity mong, MongStateCode code, Boolean isSleep) {
        this.mongStateId = mongStateId;
        this.mong = mong;
        this.code = code;
        this.isSleep = isSleep;
    }
}
