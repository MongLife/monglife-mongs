package com.monglife.mongs.app.user.store.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetConsumedProductOrderIdsRequestDto {

    private List<String> orderIds;

    @Builder
    public GetConsumedProductOrderIdsRequestDto(List<String> orderIds) {
        this.orderIds = orderIds;
    }
}
