package com.monglife.mongs.app.management.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SleepingMongResDto {

    private Long mongId;

    private Boolean isSleeping;
}
