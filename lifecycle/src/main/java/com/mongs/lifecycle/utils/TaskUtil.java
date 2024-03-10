package com.mongs.lifecycle.utils;

import com.mongs.lifecycle.code.TaskCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskUtil {

    private final Integer MIN_POOP_EXPIRATION = 5;//60 * 60 * 4;

    public Long getExpiration(TaskCode taskCode) {
        long expiration = taskCode.getExpiration();

        if (TaskCode.POOP.equals(taskCode)) {
            expiration = (int) (Math.random() * (expiration - MIN_POOP_EXPIRATION + 1) + MIN_POOP_EXPIRATION);
        }

        return expiration;
    }
}
