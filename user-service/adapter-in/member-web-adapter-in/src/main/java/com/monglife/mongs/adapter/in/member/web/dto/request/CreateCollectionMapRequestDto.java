package com.monglife.mongs.adapter.in.member.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCollectionMapRequestDto {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @Builder
    public CreateCollectionMapRequestDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
