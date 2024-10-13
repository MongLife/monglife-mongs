package com.monglife.mongs.app.management.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PoopCleanMongResDto {

    private Long mongId;

    private Integer poopCount;

    private Double exp;
}
