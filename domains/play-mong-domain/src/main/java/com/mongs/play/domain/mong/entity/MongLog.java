package com.mongs.play.domain.mong.entity;

import com.mongs.play.module.jpa.baseEntity.BaseTimeEntity;
import com.mongs.play.domain.mong.enums.MongLogCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Table(name = "mong_log")
public class MongLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_log_id")
    private Long id;
    @Column(updatable = false, nullable = false)
    private Long mongId;
    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private MongLogCode mongLogCode;
    @Column(updatable = false, nullable = false)
    private String message;
}
