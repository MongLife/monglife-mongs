package com.monglife.mongs.domain.member.entity;

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

    @JoinColumn(name = "group_code")
    private String groupCode;

    @Builder
    public ComnCodeEntity(String comnCode, String comnName, String groupCode) {
        this.comnCode = comnCode;
        this.comnName = comnName;
        this.groupCode = groupCode;
    }
}
