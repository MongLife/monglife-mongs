package com.monglife.mongs.client.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCollectionMongRequestDto {

    private String mongTypeCode;

    @Builder
    public CreateCollectionMongRequestDto(String mongTypeCode) {
        this.mongTypeCode = mongTypeCode;
    }
}
