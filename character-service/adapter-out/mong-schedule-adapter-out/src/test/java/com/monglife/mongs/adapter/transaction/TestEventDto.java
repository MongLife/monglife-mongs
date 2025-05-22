package com.monglife.mongs.adapter.transaction;

public class TestEventDto {

    private Long taskId;

    public TestEventDto() {}

    public TestEventDto(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
