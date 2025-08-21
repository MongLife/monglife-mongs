package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.port.out.vo.SearchMapVo;

import java.util.Queue;

public interface MapStoreReadPort {

    Queue<SearchMapVo> searchMapsPort(Double latitude, Double longitude, Integer radius);
}
