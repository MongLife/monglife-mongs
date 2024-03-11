package com.mongs.lifecycle.utils;

import com.mongs.lifecycle.code.TaskCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskUtil {
    @Value("${application.scheduler.poop-expiration-min}")
    private Integer POOP_EXPIRATION_MIN;

    public Long getExpiration(TaskCode taskCode) {
        long expiration = taskCode.getExpiration();

        if (TaskCode.POOP.equals(taskCode)) {
            expiration = (int) (Math.random() * (expiration - POOP_EXPIRATION_MIN + 1) + POOP_EXPIRATION_MIN);
        }

        return expiration;
    }
}
