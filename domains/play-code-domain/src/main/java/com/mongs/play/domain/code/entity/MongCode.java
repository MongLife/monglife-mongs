package com.mongs.play.domain.code.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "mong_code")
public class MongCode {
    @Id
    @Column(name = "code", nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String baseCode;
    @Column(nullable = false)
    private Integer level;
    @Column(nullable = false)
    private Integer evolutionPoint;
    @Column(nullable = false)
    private String buildVersion;
}