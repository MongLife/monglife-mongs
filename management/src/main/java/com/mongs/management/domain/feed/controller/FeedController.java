package com.mongs.management.domain.feed.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.management.domain.feed.service.FeedService;
import com.mongs.management.domain.feed.controller.dto.response.FindFeedHistoryResDto;
import com.mongs.management.domain.feed.service.vo.FeedMongVo;
import com.mongs.management.domain.feed.service.vo.FindFeedHistoryVo;
import com.mongs.management.domain.feed.controller.dto.request.FeedMongReqDto;
import com.mongs.management.domain.feed.controller.dto.response.FeedMongResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class FeedController {

    private final FeedService feedService;

    /**
     * 몽 Id 에 대해 음식 기록 목록을 반환한다.
     *
     * @param mongId 몽 Id
     * @param buildVersion 앱 빌드 버전
     * @return 음식 목록에 대한 식사 기록 (마지막으로 해당 음식을 먹은 시각)
     */
    @GetMapping("/feed/{mongId}")
    public ResponseEntity<List<FindFeedHistoryResDto>> findFeedHistory(
            @PathVariable("mongId") Long mongId,
            @RequestParam(value = "buildVersion", defaultValue = "1.0.0") String buildVersion
    ) {
        List<FindFeedHistoryVo> findFeedHistoryVoList = feedService.findFeedHistory(mongId, buildVersion);
        List<FindFeedHistoryResDto> findFeedHistoryResDtoList = findFeedHistoryVoList.stream()
                .map(FindFeedHistoryResDto::of)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(findFeedHistoryResDtoList);
    }

    /**
     * 몽 밥 먹이기
     * mongId 에 해당하는 몽에게 밥을 먹인다.
     *
     * @param feedMongReqDto 음식 Id
     * @param mongId 몽 Id
     * @param passportDetail AccessToken 을 통한 Passport 인증 객체
     * @return {@link FeedMongResDto}
     */
    @PutMapping("/feed/{mongId}")
    public ResponseEntity<FeedMongResDto> feedMong(
            @RequestBody FeedMongReqDto feedMongReqDto,
            @PathVariable("mongId") Long mongId,
            @AuthenticationPrincipal PassportDetail passportDetail
    ) {
        Long accountId = passportDetail.getId();
        String foodCode = feedMongReqDto.foodCode();

        FeedMongVo feedMongVo = feedService.feedMong(accountId, mongId, foodCode);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(FeedMongResDto.of(feedMongVo));
    }
}
