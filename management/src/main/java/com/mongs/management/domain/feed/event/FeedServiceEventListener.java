package com.mongs.management.domain.feed.event;

import com.mongs.core.enums.management.MongHistoryCode;
import com.mongs.management.domain.feed.event.vo.*;
import com.mongs.management.domain.feed.service.vo.FeedMongVo;
import com.mongs.management.global.moduleService.MongHistoryService;
import com.mongs.management.global.moduleService.NotificationService;
import com.mongs.management.global.moduleService.vo.PublishFeedVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedServiceEventListener {
    private final MongHistoryService mongHistoryService;
    private final NotificationService notificationService;

    /**
     *
     * @param event 몽 식사 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void feedMongEventListener(FeedMongEvent event) {
        FeedMongVo feedMongVo = event.feedMongVo();

        log.info("[{}] feedMongEventListener", feedMongVo.accountId());

        notificationService.publishFeed(feedMongVo.accountId(), PublishFeedVo.of(feedMongVo));

        mongHistoryService.saveMongHistory(feedMongVo.mongId(), MongHistoryCode.FEED);
    }
}
