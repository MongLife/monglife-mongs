package com.monglife.mongs.adapter.in.mong.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingEndRequestDto {

    @NotBlank
    private String trainingCode;

    @Min(1)
    @NotNull
    private Long mongId;

    @NotNull
    private Integer score;

    @Builder
    public TrainingEndRequestDto(String trainingCode, Long mongId, Integer score) {
        this.trainingCode = trainingCode;
        this.mongId = mongId;
        this.score = score;
    }
}
