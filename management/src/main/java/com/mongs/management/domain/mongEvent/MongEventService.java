package com.mongs.management.domain.mongEvent;

import com.mongs.core.code.enums.management.MongCollapse;
import com.mongs.management.domain.mongEvent.dtos.EventOccurrence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MongEventService {

    private final MongEventRepository eventRepository;

    public void save (EventOccurrence occurrence){
        eventRepository.save(MongEvent.builder()
                        .mongId(occurrence.mongId())
                        .collapse(MongCollapse.valueOf(occurrence.event()))
                        .build());
    }
}
