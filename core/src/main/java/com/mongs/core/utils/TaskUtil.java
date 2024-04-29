package com.mongs.core.utils;

import com.mongs.core.enums.lifecycle.TaskCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskUtil {
    private static final Integer POOP_EXPIRATION_MIN = 14400;

    public static Long getExpiration(TaskCode taskCode) {
        long expiration = taskCode.getExpiration();

        if (TaskCode.POOP.equals(taskCode)) {
            expiration = (int) (Math.random() * (expiration - POOP_EXPIRATION_MIN + 1) + POOP_EXPIRATION_MIN);
        }

        return expiration;
    }
}
