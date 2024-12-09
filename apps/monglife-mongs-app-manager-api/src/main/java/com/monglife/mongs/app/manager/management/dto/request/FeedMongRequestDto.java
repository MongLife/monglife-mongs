package com.monglife.mongs.app.manager.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class FeedMongRequestDto {

    @NotBlank
    private String foodTypeCode;

    @Builder
    public FeedMongRequestDto(String foodTypeCode) {
        this.foodTypeCode = foodTypeCode;
    }
}
