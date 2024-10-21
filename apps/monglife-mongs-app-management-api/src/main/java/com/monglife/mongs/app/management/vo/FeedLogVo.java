package com.monglife.mongs.app.management.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FeedLogVo {

    private Long mongId;

    private String code;

    private Boolean isCanBuy;
}
