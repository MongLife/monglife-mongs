package com.monglife.mongs.domain.match.dto.event;

import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchObserveEvent {

    public static MatchObserveEvent of (MatchRoomEntity matchRoomEntity) {

    }
}
