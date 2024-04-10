package com.mongs.management.domain.management.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.management.domain.management.service.ManagementService;
import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/management/stress")
public class StressController {

    private final ManagementService managementService;

    @PostMapping("/{count}")
    public ResponseEntity<List<Long>> registerMong(
            @PathVariable Integer count,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        List<Long> mongIdList = new ArrayList<>();
        for (int i = 0; i < Math.min(count, 100); i++) {
            String name = "테스트_" + i;
            String sleepStart = "08:00";
            String sleepEnd = "22:00";
            MongVo mongVo = managementService.registerMong(accountId, name, sleepStart, sleepEnd);
            mongIdList.add(mongVo.mongId());
        }
        return ResponseEntity.ok().body(mongIdList);
    }

    @PutMapping("/{start}/{end}")
    public ResponseEntity<Object> evolutionMong(
            @PathVariable("start") Long start,
            @PathVariable("end") Long end,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        for (long mongId = start; mongId <= end; mongId++) {
            try {
                managementService.evolutionMong(accountId, mongId);
            } catch (Exception ignored) {}
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @DeleteMapping("/{start}/{end}")
    public ResponseEntity<Object> deleteMong(
            @PathVariable("start") Long start,
            @PathVariable("end") Long end,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();

        for (long mongId = start; mongId <= end; mongId++) {
            try {
                managementService.deleteMong(accountId, mongId);
            } catch (Exception ignored) {}
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }
}
