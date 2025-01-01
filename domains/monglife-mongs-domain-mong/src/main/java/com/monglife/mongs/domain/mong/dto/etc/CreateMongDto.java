package com.monglife.mongs.domain.mong.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMongDto {

    private Long mongId;

    private String mongTypeCode;

    @Builder
    public CreateMongDto(Long mongId, String mongTypeCode) {
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
    }
}
