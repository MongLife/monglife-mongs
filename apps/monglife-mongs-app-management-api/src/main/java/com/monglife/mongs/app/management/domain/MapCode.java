package com.monglife.mongs.app.management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapCode {

    @Id
    @Column(name = "code", nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;
}