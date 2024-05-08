package com.mongs.play.app.management.worker.controller;

import com.mongs.play.app.management.worker.service.ManagementWorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/worker/management")
@RestController
@RequiredArgsConstructor
public class ManagementWorkerController {

    private final ManagementWorkerService managementWorkerService;

    @GetMapping("")
    public ResponseEntity<Object> test() {
        managementWorkerService.decreaseWeight(1L, 10.0);
        return ResponseEntity.ok().body(null);
    }
}
