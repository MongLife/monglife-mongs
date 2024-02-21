package com.mongs.management.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * name: string
 * sleepStart: string (HH:mm)
 * sleepEnd: string (HH:mm)
 */
public record InitMong (
    String name,
    String sleepStart,
    String sleepEnd
){
}
