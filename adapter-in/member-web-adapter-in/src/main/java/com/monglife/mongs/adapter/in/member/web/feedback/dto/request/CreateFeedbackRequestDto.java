package com.monglife.mongs.adapter.in.member.web.feedback.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateFeedbackRequestDto {

    @NotBlank
    private String deviceName;

    @NotBlank
    private String title;

    @NotNull
    private String content;

    @Builder
    public CreateFeedbackRequestDto(String deviceName, String title, String content) {
        this.deviceName = deviceName;
        this.title = title;
        this.content = content;
    }
}
