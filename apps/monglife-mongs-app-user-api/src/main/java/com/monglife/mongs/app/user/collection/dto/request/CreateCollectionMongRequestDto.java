package com.monglife.mongs.app.user.collection.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCollectionMongRequestDto {

    @NotBlank
    private String mongTypeCode;

    @Builder
    public CreateCollectionMongRequestDto(String mongTypeCode) {
        this.mongTypeCode = mongTypeCode;
    }
}
