package com.monglife.mongs.app.manager.management.dto.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @DateTimeFormat(pattern = "hh:MM:ss")
    private LocalTime sleepAt;

    @NotBlank
    @DateTimeFormat(pattern = "hh:MM:ss")
    private LocalTime wakeupAt;


    @Builder
    public CreateMongRequestDto(String name, LocalTime sleepAt, LocalTime wakeupAt) {
        this.name = name;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
    }
}
