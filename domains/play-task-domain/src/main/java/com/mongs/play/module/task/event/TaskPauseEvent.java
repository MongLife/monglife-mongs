package com.mongs.play.module.task.event;

import com.mongs.play.module.task.enums.TaskCode;
import lombok.*;

@Getter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskPauseEvent {
    private TaskCode taskCode;
    private Long mongId;
}
