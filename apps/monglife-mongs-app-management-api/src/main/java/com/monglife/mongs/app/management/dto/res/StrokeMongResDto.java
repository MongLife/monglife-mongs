package com.monglife.mongs.app.management.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StrokeMongResDto {

    private Long mongId;

    private Double exp;
}
