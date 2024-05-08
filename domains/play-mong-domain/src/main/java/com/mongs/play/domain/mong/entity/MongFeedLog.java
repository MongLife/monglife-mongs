package com.mongs.play.domain.mong.entity;

import com.mongs.play.module.jpa.baseEntity.BaseTimeEntity;
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
public class MongFeedLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mong_feed_log_id")
    private Long id;
    @Column(updatable = false, nullable = false)
    private Long mongId;
    @Column(updatable = false, nullable = false)
    private String code;
    @Column(updatable = false, nullable = false)
    private Integer price;
}
