package com.monglife.mongs.adapter.out.battle.persistence.service;

import com.monglife.mongs.adapter.out.battle.persistence.entity.QueuePlayerEntity;
import com.monglife.mongs.adapter.out.battle.persistence.repository.MatchRepository;
import com.monglife.mongs.adapter.out.battle.persistence.repository.QueuePlayerRepository;
import com.monglife.mongs.application.battle.port.out.MatchPersistencePort;
import com.monglife.mongs.application.battle.port.out.vo.CreateMatchVo;
import com.monglife.mongs.application.battle.port.out.vo.CreateQueuePlayerVo;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.QueuePlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchPersistenceService implements MatchPersistencePort {

    private final MatchRepository matchRepository;

    private final QueuePlayerRepository queuePlayerRepository;

    /**
     * 매치 대기열 조회
     * @param mongId 몽 ID
     * @param accountId 계정 ID
     * @param deviceId 기기 ID
     * @return 매치 대기열 도메인 객체
     */
    @Override
    public Optional<QueuePlayer> getQueuePlayerPort(Long mongId, Long accountId, String deviceId) {
        return queuePlayerRepository.findByMongIdAndAccountIdAndDeviceId(mongId, accountId, deviceId)
                .map(QueuePlayerEntity::toDomain)
                .or(Optional::empty);
    }

    /**
     * 매치 대기열 목록 조회
     * @param matchPlayerCount 매치 플레이어 수
     * @param expiredSeconds 봇 매칭 대기 시간
     * @return 매치 대기열 도메인 객체 목록
     */
    @Override
    public List<QueuePlayer> getQueuePlayersPort(Integer matchPlayerCount, Long expiredSeconds) {

        LocalDateTime now = LocalDateTime.now();

        Set<QueuePlayerEntity> queuePlayerEntities = queuePlayerRepository.findByCount(matchPlayerCount);

        if (queuePlayerEntities.size() < matchPlayerCount) {
            queuePlayerEntities = queuePlayerEntities.stream()
                    .filter(queuePlayerEntity -> {
                        LocalDateTime expiredAt = queuePlayerEntity.getCreatedAt().plusSeconds(expiredSeconds);
                        return now.isEqual(expiredAt) || now.isAfter(expiredAt);
                    })
                    .collect(Collectors.toSet());
        }

        queuePlayerEntities.forEach(queuePlayerRepository::delete);

        return queuePlayerEntities.stream()
                .map(QueuePlayerEntity::toDomain)
                .toList();
    }

    /**
     * 매치 대기열 등록
     * @param createQueuePlayerVo 매치 대기열 등록 Vo
     * @return 매치 대기열 도메인 객체
     */
    @Override
    public Optional<QueuePlayer> createQueuePlayerPort(CreateQueuePlayerVo createQueuePlayerVo) {

        QueuePlayerEntity queuePlayerEntity = QueuePlayerEntity.builder()
                .mongId(createQueuePlayerVo.getMongId())
                .deviceId(createQueuePlayerVo.getDeviceId())
                .accountId(createQueuePlayerVo.getAccountId())
                .createdAt(LocalDateTime.now())
                .build();

        return Optional.of(queuePlayerRepository.save(queuePlayerEntity).toDomain());
    }

    /**
     * 매치 대기열 삭제
     * @param queuePlayer 삭제할 매치 대기열 도메인 객체
     * @return 매치 대기열 도메인 객체
     */
    @Override
    public Optional<QueuePlayer> deleteQueuePlayerPort(QueuePlayer queuePlayer) {

        Optional<QueuePlayerEntity> queuePlayerEntityOptional = queuePlayerRepository.findByMongIdAndAccountIdAndDeviceId(queuePlayer.getMongId(), queuePlayer.getAccountId(), queuePlayer.getDeviceId());

        if (queuePlayerEntityOptional.isPresent()) {
            QueuePlayerEntity queuePlayerEntity = queuePlayerEntityOptional.get();
            queuePlayerRepository.delete(queuePlayerEntity);
            return Optional.of(queuePlayerEntity.toDomain());
        }

        return Optional.empty();
    }

    /**
     * 매치 조회
     * @param matchId 매치 ID
     * @return 매치 도메인 객체
     */
    @Override
    public Optional<Match> getMatchPort(Long matchId) {
        return Optional.empty();
    }

    /**
     * 매치 등록
     * @param createMatchVo 매치 등록 Vo
     * @return 매치 도메인 객체
     */
    @Override
    public Optional<Match> createMatchPort(CreateMatchVo createMatchVo) {
        return Optional.empty();
    }

    /**
     * 매치 동기화
     * @param matchDto 매치 도메인 객체
     * @return 매치 도메인 객체
     */
    @Override
    public Optional<Match> saveMatchPort(Match matchDto) {
        return Optional.empty();
    }
}
