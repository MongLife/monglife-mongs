package com.monglife.mongs.module.jpa.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "mongs_comn_code")
public class ComnCodeEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_code")
    private GroupCodeEntity groupCode;

    @Builder
    public ComnCodeEntity(String code, String name, GroupCodeEntity groupCode) {
        this.code = code;
        this.name = name;
        this.groupCode = groupCode;
    }
}
