package com.monglife.mongs.domain.battle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_group_code")
public class GroupCodeEntity {

    @Id
    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "group_name")
    private String groupName;

    @Builder
    public GroupCodeEntity(String groupCode, String groupName) {
        this.groupCode = groupCode;
        this.groupName = groupName;
    }
}
