package com.monglife.mongs.adapter.out.member.publish.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberSlotCountPublishDto {

    private Long accountId;

    private Integer slotCount;

    @Builder
    public MemberSlotCountPublishDto(Long accountId, Integer slotCount) {
        this.accountId = accountId;
        this.slotCount = slotCount;
    }
}
