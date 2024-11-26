package com.monglife.mongs.app.manager.management.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class FeedMongRequestDto {

    private String foodTypeCode;


    @Builder
    public FeedMongRequestDto(String foodTypeCode) {
        this.foodTypeCode = foodTypeCode;
    }
}
