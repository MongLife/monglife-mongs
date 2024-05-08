package com.mongs.play.module.code.vo;

import com.mongs.play.module.code.entity.FoodCode;
import com.mongs.play.module.code.entity.MapCode;
import com.mongs.play.module.code.entity.MongCode;
import lombok.Builder;

import java.util.List;

@Builder
public record CodeVo(
        List<MapCode> mapCodeList,
        List<MongCode> mongCodeList,
        List<FoodCode> foodCodeList
) {
}
