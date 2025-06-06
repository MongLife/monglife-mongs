package com.monglife.mongs.adapter.out.battle.persistence.service;

import com.monglife.mongs.adapter.out.battle.persistence.entity.MatchEntity;
import com.monglife.mongs.adapter.out.battle.persistence.repository.MatchRepository;
import com.monglife.mongs.application.battle.port.out.MatchReadPort;
import com.monglife.mongs.domain.battle.model.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchReadService implements MatchReadPort {

    private final MatchRepository matchRepository;

    /**
     * 매치 조회
     * @param matchId 매치 ID
     * @return 매치 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Match> getMatchPort(Long matchId) {
        return matchRepository.findByMatchId(matchId)
                .map(MatchEntity::toDomain)
                .or(Optional::empty);
    }
}
