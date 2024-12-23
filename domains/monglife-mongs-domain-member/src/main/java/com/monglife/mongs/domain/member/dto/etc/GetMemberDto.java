package com.monglife.mongs.domain.member.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetMemberDto {

    private Long accountId;

    private Integer slotCount;

    private Integer starPoint;

    @Builder
    public GetMemberDto(Long accountId, Integer slotCount, Integer starPoint) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
    }
}
