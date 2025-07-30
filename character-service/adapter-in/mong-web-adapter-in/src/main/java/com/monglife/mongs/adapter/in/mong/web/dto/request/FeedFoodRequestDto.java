package com.monglife.mongs.adapter.in.mong.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedFoodRequestDto {

    @NotBlank
    private String foodCode;

    @Builder
    public FeedFoodRequestDto(String foodCode) {
        this.foodCode = foodCode;
    }
}
