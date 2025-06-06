package com.monglife.mongs.adapter.out.mong.schedule.initializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.utils.CommonUtil;
import com.monglife.module.common.logging.annotation.DisableLogging;
import com.monglife.module.common.logging.enums.LogType;
import com.monglife.mongs.adapter.out.mong.schedule.dto.InitializerLogDto;
import com.monglife.mongs.adapter.out.mong.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Order(Integer.MIN_VALUE)
@Component
public class ShutdownInitializer implements ApplicationListener<ContextClosedEvent> {

    private final TaskService taskService;

    private final ObjectMapper objectMapper;

    public ShutdownInitializer(@Autowired TaskService taskService) {
        this.taskService = taskService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    @DisableLogging
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        if (contextClosedEvent.getApplicationContext().getParent() == null) {
            AtomicInteger taskCount = new AtomicInteger();
            List<Long> taskIds = new ArrayList<>();

            taskService.appStopPauseAllTask().forEach(taskEntity -> {
                taskCount.getAndIncrement();
                taskIds.add(taskEntity.getTaskId());
            });

            String className = this.getClass().getName();
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

            InitializerLogDto initializerLogDto = InitializerLogDto.builder()
                    .traceId(CommonUtil.randomId())
                    .traceOffset(0)
                    .entryMethod(String.format("%s#%s", className, methodName))
                    .className(className)
                    .method(methodName)
                    .logType(LogType.METHOD_CALL)
                    .taskCount(taskCount.get())
                    .taskIds(taskIds)
                    .build();

            try {
                log.info("{}", objectMapper.writeValueAsString(initializerLogDto));
            } catch (JsonProcessingException ignored) {}
        }
    }
}