package com.mongs.play.domain.code.vo;

import com.mongs.play.domain.code.entity.FeedbackCode;
import com.mongs.play.domain.code.entity.FoodCode;
import com.mongs.play.domain.code.entity.MapCode;
import com.mongs.play.domain.code.entity.MongCode;
import lombok.Builder;

import java.util.List;

@Builder
public record CodeVo(
        List<MapCode> mapCodeList,
        List<MongCode> mongCodeList,
        List<FoodCode> foodCodeList,
        List<FeedbackCode> feedbackCodeList
) {
}
