package com.mongs.lifecycle.service_;

import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.repository.EventTaskRepository;
import com.mongs.lifecycle.repository.MongEventRepository;
import com.mongs.lifecycle.repository.MongRepository;
import com.mongs.lifecycle.utils.MongEventUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
//@RequiredArgsConstructor
public class LifecycleService {
    private final TaskService taskService;


    @Autowired
    public LifecycleService(
            TaskService taskService,
            TaskActiveService taskActiveService,
            MongEventRepository mongEventRepository,
            MongRepository mongRepository
    ) {
        this.taskService = taskService;
        mongEventRepository.deleteAll();
        for (long mongId = 1; mongId <= 1000; mongId++) {
            mongRepository.save(Mong.builder()
                    .accountId(1L)
                    .name("(" + mongId + ") 테스트 몽")
                    .build());
        }
    }

    public void statusEvent(Long mongId) {
        taskService.registerTask(mongId, MongEventCode.HEALTHY_DOWN);
    }

    public void sleepEvent(Long mongId) {

    }

    public void wakeupEvent(Long mongId) {

    }

    public void evolutionEvent(Long mongId) {

    }

    public void graduationEvent(Long mongId) {

    }
}
