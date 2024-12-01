package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.domain.mong.dto.etc.GetFeedItemDto;
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

    private List<GetFeedItemDto> feedItems;


    @Builder
    public GetFeedItemsResponseDto(Long mongId, List<GetFeedItemDto> feedItems) {
        this.mongId = mongId;
        this.feedItems = feedItems;
    }
}
