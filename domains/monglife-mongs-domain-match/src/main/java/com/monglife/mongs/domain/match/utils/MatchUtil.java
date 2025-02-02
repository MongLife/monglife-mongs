package com.monglife.mongs.domain.match.utils;

import com.monglife.mongs.domain.match.entity.MatchPlayerEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MatchUtil {

    /**
     * 배틀 플레이어 랭킹
     * @param matchPlayerEntities 배틀 플레이어 엔티티 목록
     * @return 배틀 플레이어 엔티티 등수 기준 정렬 리스트
     */
    public static List<MatchPlayerEntity> rankMatchPlayer(Set<MatchPlayerEntity> matchPlayerEntities) {

        // 나간 배틀 플레이어
        List<MatchPlayerEntity> rankMatchPlayerEntities = matchPlayerEntities.stream()
                .filter(matchPlayerEntity -> !matchPlayerEntity.getIsEnter())
                .sorted((bp1, bp2) -> {
                    if (bp1.getHp().equals(bp2.getHp())) {
                        return bp1.getExitDt().compareTo(bp2.getExitDt());
                    }
                    return bp2.getHp().compareTo(bp1.getHp());
                })
                .collect(Collectors.toList());

        // 나가지 않은 배틀 플레이어
        matchPlayerEntities.stream()
                .filter(MatchPlayerEntity::getIsEnter)
                // 역순 정렬
                .sorted((bp1, bp2) -> {
                    if (bp1.getHp().equals(bp2.getHp())) {
                        return bp2.getEnterDt().compareTo(bp1.getEnterDt());
                    }
                    return bp1.getHp().compareTo(bp2.getHp());
                })
                // 순위 리스트 앞에서 부터 삽입 (하위 등수 부터 저장)
                .forEachOrdered(matchPlayerEntity -> rankMatchPlayerEntities.add(0, matchPlayerEntity));

        return rankMatchPlayerEntities;
    }
}
