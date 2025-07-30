package com.monglife.mongs.adapter.out.member.publish.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberStarPointPublishDto {

    private Long accountId;

    private Integer starPoint;

    @Builder
    public MemberStarPointPublishDto(Long accountId, Integer starPoint) {
        this.accountId = accountId;
        this.starPoint = starPoint;
    }
}
