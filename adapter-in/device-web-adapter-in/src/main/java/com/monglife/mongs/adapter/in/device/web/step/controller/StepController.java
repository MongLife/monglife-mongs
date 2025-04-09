package com.monglife.mongs.adapter.in.device.web.step.controller;

import com.monglife.mongs.application.device.port.service.StepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;
}
