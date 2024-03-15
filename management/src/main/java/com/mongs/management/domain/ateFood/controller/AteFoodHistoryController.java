package com.mongs.management.domain.ateFood.controller;

import com.mongs.management.domain.ateFood.service.AteFoodHistoryService;
import com.mongs.management.domain.ateFood.service.dto.FoodHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class AteFoodHistoryController {

    private final AteFoodHistoryService ateFoodHistoryService;

    @GetMapping("/feedHistory/{mongId}")
    public ResponseEntity<List<FoodHistory>> findAteFoodHistory(
            @PathVariable("mongId") Long mongId,
            @RequestParam("version") Long version
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ateFoodHistoryService.findAteFoodHistory(mongId, version));
    }
}
