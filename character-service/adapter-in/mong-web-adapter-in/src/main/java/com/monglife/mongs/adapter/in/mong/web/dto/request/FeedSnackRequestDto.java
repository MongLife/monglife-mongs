package com.monglife.mongs.adapter.in.mong.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedSnackRequestDto {

    @NotBlank
    private String snackCode;

    @Builder
    public FeedSnackRequestDto(String snackCode) {
        this.snackCode = snackCode;
    }
}
