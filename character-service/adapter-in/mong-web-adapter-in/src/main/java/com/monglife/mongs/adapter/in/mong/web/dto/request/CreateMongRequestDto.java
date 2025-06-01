package com.monglife.mongs.adapter.in.mong.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateMongRequestDto {

    @NotBlank
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "hh:MM:ss")
    private LocalTime sleepAt;

    @NotNull
    @DateTimeFormat(pattern = "hh:MM:ss")
    private LocalTime wakeupAt;

    @Builder
    public CreateMongRequestDto(String name, LocalTime sleepAt, LocalTime wakeupAt) {
        this.name = name;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
    }
}
