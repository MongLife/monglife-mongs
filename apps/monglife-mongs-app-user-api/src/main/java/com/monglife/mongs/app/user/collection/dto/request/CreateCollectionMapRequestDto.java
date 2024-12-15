package com.monglife.mongs.app.user.collection.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCollectionMapRequestDto {

    @NotBlank
    private String mapTypeCode;

    @Builder
    public CreateCollectionMapRequestDto(String mapTypeCode) {
        this.mapTypeCode = mapTypeCode;
    }
}
