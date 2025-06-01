package com.monglife.mongs.adapter.in.mong.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteMongResponseDto {

    private Long mongId;

    @Builder
    public DeleteMongResponseDto(Long mongId) {
        this.mongId = mongId;
    }
}
