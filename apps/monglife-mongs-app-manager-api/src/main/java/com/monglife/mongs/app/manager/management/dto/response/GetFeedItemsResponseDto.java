package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.domain.mong.vo.FeedItemVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetFeedItemsResponseDto {

    private Long mongId;

    private List<FeedItemVo> feedItems;

    @Builder
    public GetFeedItemsResponseDto(Long mongId, List<FeedItemVo> feedItems) {
        this.mongId = mongId;
        this.feedItems = feedItems;
    }
}
