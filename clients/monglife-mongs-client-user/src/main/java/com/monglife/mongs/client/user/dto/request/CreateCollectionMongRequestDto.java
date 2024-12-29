package com.monglife.mongs.client.user.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateCollectionMongRequestDto {

    private String mongTypeCode;

    @Builder
    public CreateCollectionMongRequestDto(String mongTypeCode) {
        this.mongTypeCode = mongTypeCode;
    }
}
