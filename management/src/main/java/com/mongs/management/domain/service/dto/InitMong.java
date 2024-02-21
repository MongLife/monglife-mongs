package com.mongs.management.domain.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * name: string
 * sleepStart: string (HH:mm)
 * sleepEnd: string (HH:mm)
 */
public record InitMong (
    String name,
    LocalDateTime sleepStart,
    LocalDateTime sleepEnd
){
}
