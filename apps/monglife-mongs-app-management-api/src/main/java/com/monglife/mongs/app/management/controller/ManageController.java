package com.monglife.mongs.app.management.controller;

import com.monglife.mongs.app.management.dto.req.RegisterMongReqDto;
import com.monglife.mongs.app.management.dto.res.*;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/management")
@RequiredArgsConstructor
@RestController
public class ManageController {

    @GetMapping("/feed/{mongId}")
    public ResponseEntity<List<FindFeedItemResDto>> findFeedItem(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {
        return null;
    }

    @GetMapping("")
    public ResponseEntity<List<FindMongResDto>> findMong(@AuthenticationPrincipal Passport passport) {
        return null;
    }

    @GetMapping("/{mongId}")
    public ResponseEntity<FindMongResDto> findMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {
        return null;
    }

    @PostMapping("")
    public ResponseEntity<RegisterMongResDto> registerMong(@AuthenticationPrincipal Passport passport, @RequestBody RegisterMongReqDto registerMongReqDto) {
        return null;
    }

    @DeleteMapping("/{mongId}")
    public ResponseEntity<DeleteMongResDto> deleteMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {
        return null;
    }

    @PutMapping("/sleep/{mongId}")
    public ResponseEntity<SleepingMongResDto> sleepingMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {
        return null;
    }

    @PutMapping("/poopClean/{mongId}")
    public ResponseEntity<PoopCleanMongResDto> poopClean(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {
        return null;
    }

    @PutMapping("/graduate/{mongId}")
    public ResponseEntity<GraduateMongResDto> graduateMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {
        return null;
    }

    @PutMapping("/evolution/{mongId}")
    public ResponseEntity<EvolutionMongResDto> evolutionMong(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") Long mongId) {
        return null;
    }
}
