package com.monglife.mongs.adapter.in.device.sub.step.consumer;

import com.monglife.mongs.application.device.port.in.StepUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StepRollbackConsumer {

    private final StepUseCase stepUseCase;
}
