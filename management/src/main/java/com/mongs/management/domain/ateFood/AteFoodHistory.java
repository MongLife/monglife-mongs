package com.mongs.management.domain.ateFood;

import com.mongs.core.time.BaseTimeEntity;
import com.mongs.management.domain.mong.entity.Mong;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AteFoodHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ateId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mong mong;

    @Column(unique = true)
    private String foodName;
}
