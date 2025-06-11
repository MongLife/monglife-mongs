package com.monglife.mongs.adapter.out.mong.schedule.initializer;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.common.logging.annotation.DisableLogging;
import com.monglife.module.common.logging.enums.BasicLogType;
import com.monglife.module.common.logging.enums.LoggerType;
import com.monglife.module.common.logging.utils.LoggingUtil;
import com.monglife.mongs.adapter.out.mong.schedule.dto.InitializerLogDto;
import com.monglife.mongs.adapter.out.mong.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Order(Integer.MIN_VALUE)
@Component
public class StartupInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final TaskService taskService;

    private final LoggingUtil loggingUtil;

    @Autowired
    public StartupInitializer(TaskService taskService, LoggingUtil loggingUtil) {
        this.taskService = taskService;
        this.loggingUtil = loggingUtil;
    }

    @Override
    @DisableLogging
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (applicationReadyEvent.getApplicationContext().getParent() == null) {
            AtomicInteger taskCount = new AtomicInteger();
            List<Long> taskIds = new ArrayList<>();

            taskService.appStopResumeAllTask().forEach(taskEntity -> {
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
                    .logType(BasicLogType.METHOD_CALL)
                    .taskCount(taskCount.get())
                    .taskIds(taskIds)
                    .build();

            loggingUtil.printInfoLog(initializerLogDto, LoggerType.CONSOLE_LOGGER);
        }
    }
}
