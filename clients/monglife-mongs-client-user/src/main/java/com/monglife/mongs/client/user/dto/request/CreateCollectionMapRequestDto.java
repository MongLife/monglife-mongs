package com.monglife.mongs.client.user.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateCollectionMapRequestDto {

    private String mapTypeCode;

    @Builder
    public CreateCollectionMapRequestDto(String mapTypeCode) {
        this.mapTypeCode = mapTypeCode;
    }
}
