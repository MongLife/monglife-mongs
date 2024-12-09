package com.monglife.mongs.module.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_comn_code")
public class ComnCodeEntity {

    @Id
    @Column(name = "comn_code")
    private String comnCode;

    @Column(name = "comn_name")
    private String comnName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_code")
    private GroupCodeEntity groupCode;

    @Builder
    public ComnCodeEntity(String comnCode, String comnName, GroupCodeEntity groupCode) {
        this.comnCode = comnCode;
        this.comnName = comnName;
        this.groupCode = groupCode;
    }
}
