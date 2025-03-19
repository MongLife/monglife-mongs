package com.monglife.mongs.app.user.store.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetConsumedProductOrderIdsResponseDto {

    private List<String> orderIds;

    @Builder
    public GetConsumedProductOrderIdsResponseDto(List<String> orderIds) {
        this.orderIds = orderIds;
    }
}
