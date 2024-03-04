package com.mongs.management.domain.food;

import com.mongs.management.config.BaseTimeEntity;
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
public class Food extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "foodId")
    private Long id;
    private String foodCode;
    private String detailCode;
    private int fullness;
    @Column(unique = true)
    private String name;
    private int price;
}
