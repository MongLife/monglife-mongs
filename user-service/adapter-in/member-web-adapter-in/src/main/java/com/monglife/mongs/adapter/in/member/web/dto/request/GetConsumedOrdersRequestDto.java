package com.monglife.mongs.adapter.in.member.web.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetConsumedOrdersRequestDto {

    @NotEmpty
    private List<String> socialOrderIds;

    @Builder
    public GetConsumedOrdersRequestDto(List<String> socialOrderIds) {
        this.socialOrderIds = socialOrderIds;
    }
}
