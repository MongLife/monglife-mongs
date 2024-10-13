package com.monglife.mongs.app.management.controller;

import com.monglife.mongs.app.management.dto.req.FeedMongReqDto;
import com.monglife.mongs.app.management.dto.req.TrainingMongReqDto;
import com.monglife.mongs.app.management.dto.res.FeedMongResDto;
import com.monglife.mongs.app.management.dto.res.StrokeMongResDto;
import com.monglife.mongs.app.management.dto.res.TrainingMongResDto;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/management")
@RequiredArgsConstructor
@RestController
public class ActivityController {

    @PutMapping("/stroke/{mongId}")
    public ResponseEntity<StrokeMongResDto> strokeMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {
        return null;
    }

    @PutMapping("/training/{mongId}")
    public ResponseEntity<TrainingMongResDto> trainingMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId, @RequestBody TrainingMongReqDto trainingMongReqDto) {
        return null;
    }

    @PostMapping("/feed/{mongId}")
    public ResponseEntity<FeedMongResDto> feedMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId, @RequestBody FeedMongReqDto feedMongReqDto) {
        return null;
    }
}
