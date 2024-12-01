package com.monglife.mongs.domain.mong.dto.etc;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMongDto {

    private Long mongId;

    private String mongName;

    private String mongCode;


    public static GetMongDto of(MongEntity mongEntity) {
        return GetMongDto.builder()
                .mongId(mongEntity.getMongId())
                .mongName(mongEntity.getMongName())
                .mongCode(mongEntity.getType().getMongCode().getComnCode())
                .build();
    }
}
