package com.mongs.lifecycle.repository;

import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.thread.BasicTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventTaskRepository {

    private final Map<String, BasicTask> eventTaskMap;

    private EventTaskRepository() {
        this.eventTaskMap = new ConcurrentHashMap<>();
    }

    public void save(BasicTask basicTask) throws RuntimeException {
        this.eventTaskMap.put(basicTask.getEvent().getEventId(), basicTask);
    }

    public BasicTask findById(String eventId) throws EventTaskException {
        if (!eventTaskMap.containsKey(eventId)) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK);
        }
        return this.eventTaskMap.get(eventId);
    }

    public void deleteById(String eventId) throws EventTaskException {
        if (!eventTaskMap.containsKey(eventId)) {
            throw new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK);
        }
        eventTaskMap.remove(eventId);
    }
}
