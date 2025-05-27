package com.monglife.mongs.adapter.in.mong.web.controller;

import com.monglife.mongs.application.mong.port.in.ManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementUseCase managementUseCase;
}
