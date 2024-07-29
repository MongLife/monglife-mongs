package com.mongs.play.domain.task.event;

import com.mongs.play.domain.task.enums.TaskCode;
import lombok.*;

@Getter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStopEvent {
    private TaskCode taskCode;
    private Long mongId;
    private Long expiration;
    private Long duringSeconds;
}
