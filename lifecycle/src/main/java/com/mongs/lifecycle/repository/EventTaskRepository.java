package com.mongs.lifecycle.repository;

import com.mongs.lifecycle.code.EventStatusCode;
import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.task.BasicTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventTaskRepository {

    private final Map<String, BasicTask> eventTaskMap;

    private EventTaskRepository() {
        this.eventTaskMap = new ConcurrentHashMap<>();
    }

    public void save(BasicTask basicTask) {
        this.eventTaskMap.put(basicTask.getEvent().getEventId(), basicTask);
    }

    public Optional<BasicTask> findById(String eventId) {
        if (!eventTaskMap.containsKey(eventId)) {
            return Optional.empty();
        }
        return Optional.of(this.eventTaskMap.get(eventId));
    }

    public void deleteById(String eventId) {
        eventTaskMap.remove(eventId);
    }
}
