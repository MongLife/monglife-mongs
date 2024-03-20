package com.mongs.management.domain.feedHistory.controller;

import com.mongs.management.domain.feedHistory.service.FeedHistoryService;
import com.mongs.management.domain.feedHistory.controller.dto.FindFeedHistoryResDto;
import com.mongs.management.domain.feedHistory.service.vo.FindFeedHistoryVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class FeedHistoryController {

    private final FeedHistoryService feedHistoryService;

    @GetMapping("/feedHistory/{mongId}")
    public ResponseEntity<List<FindFeedHistoryResDto>> findAteFoodHistory(@PathVariable("mongId") Long mongId, @RequestParam("version") Long version) {

        List<FindFeedHistoryVo> findFeedHistoryVoList = feedHistoryService.findFeedHistory(mongId, version);
        List<FindFeedHistoryResDto> findFeedHistoryResDtoList = findFeedHistoryVoList.stream()
                .map(FindFeedHistoryResDto::of)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(findFeedHistoryResDtoList);
    }
}
