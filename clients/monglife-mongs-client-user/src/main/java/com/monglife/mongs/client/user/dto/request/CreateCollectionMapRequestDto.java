package com.monglife.mongs.client.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCollectionMapRequestDto {

    private String mapTypeCode;

    @Builder
    public CreateCollectionMapRequestDto(String mapTypeCode) {
        this.mapTypeCode = mapTypeCode;
    }
}
