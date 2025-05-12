package com.monglife.mongs.application.battle.port.in.service;

import com.monglife.mongs.application.battle.port.exception.InvalidCreateMatchException;
import com.monglife.mongs.application.battle.port.exception.InvalidCreateQueuePlayerException;
import com.monglife.mongs.application.battle.port.exception.NotExistsQueuePlayerException;
import com.monglife.mongs.application.battle.port.in.QueueUseCase;
import com.monglife.mongs.application.battle.port.in.command.CreateQueuePlayerCommand;
import com.monglife.mongs.application.battle.port.in.command.DeleteQueuePlayerCommand;
import com.monglife.mongs.application.battle.port.in.command.MatchingQueuePlayersCommand;
import com.monglife.mongs.application.battle.port.out.MatchMongPersistencePort;
import com.monglife.mongs.application.battle.port.out.MatchPersistencePort;
import com.monglife.mongs.application.battle.port.out.MatchPublishPort;
import com.monglife.mongs.application.battle.port.out.QueuePublishPort;
import com.monglife.mongs.application.battle.port.out.vo.CreateMatchVo;
import com.monglife.mongs.application.battle.port.out.vo.CreateQueuePlayerVo;
import com.monglife.mongs.domain.model.Match;
import com.monglife.mongs.domain.model.MatchMong;
import com.monglife.mongs.domain.model.MatchPlayer;
import com.monglife.mongs.domain.model.QueuePlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueueService implements QueueUseCase {

    private final MatchPersistencePort matchPersistencePort;

    private final MatchMongPersistencePort matchMongPersistencePort;

    private final MatchPublishPort matchPublishPort;

    private final QueuePublishPort queuePublishPort;

    /**
     * 매치 대기열 생성
     */
    @Override
    @Transactional
    public QueuePlayer createQueuePlayerUseCase(CreateQueuePlayerCommand command) {

        CreateQueuePlayerVo createQueuePlayerVo = CreateQueuePlayerVo.builder()
                .mongId(command.getMongId())
                .deviceId(command.getDeviceId())
                .accountId(command.getAccountId())
                .build();

        return matchPersistencePort.createQueuePlayerPort(createQueuePlayerVo)
                .orElseThrow(InvalidCreateQueuePlayerException::new);
    }

    /**
     * 매치 대기열 삭제
     */
    @Override
    @Transactional
    public QueuePlayer deleteQueuePlayerUseCase(DeleteQueuePlayerCommand command) {

        QueuePlayer queuePlayer = matchPersistencePort.getQueuePlayerPort(command.getMongId(), command.getAccountId(), command.getDeviceId())
                .orElseThrow(NotExistsQueuePlayerException::new);

        return matchPersistencePort.deleteQueuePlayerPort(queuePlayer)
                .orElseThrow(NotExistsQueuePlayerException::new);
    }

    /**
     * 매치 대기열 기준 플레이어 매칭
     */
    @Override
    @Transactional
    public void matchingQueuePlayersUseCase(MatchingQueuePlayersCommand command) {

        List<QueuePlayer> queuePlayers = matchPersistencePort.getQueuePlayersPort(command.getMatchPlayerCount(), QueuePlayer.getExpiredSeconds());

        // 매칭 가능한 매치 대기열 없는 경우
        if (queuePlayers.isEmpty()) {
            throw new NotExistsQueuePlayerException();
        }

        // 매치 몽 정보가 없는 매치 대기열 도메인 목록
        List<QueuePlayer> notExistsMatchMongQueuePlayer = new ArrayList<>();
        // 매칭이 완료된 매치 플레이어 목록
        List<MatchPlayer> matchPlayers = new ArrayList<>();

        queuePlayers.forEach(queuePlayer -> {
            // 매치 몽 조회
            Optional<MatchMong> matchMongOptional = matchMongPersistencePort.getMatchMongPort(queuePlayer.getMongId());

            // 매치 몽이 존재 하지 않는 경우
            if (matchMongOptional.isEmpty()) {
                // 매치 몽이 존재 하지 않는 매치 대기열 저장
                notExistsMatchMongQueuePlayer.add(queuePlayer);
            }
            // 매치 몽이 존재 하는 경우
            else {
                // 매치 몽을 매치 플레이어 변환 후 저장
                MatchMong matchMong = matchMongOptional.get();
                matchPlayers.add(MatchPlayer.generateMatchPlayer(queuePlayer, matchMong));
            }
        });

        // 존재 하지 않는 매치 몽이 있는 경우, 존재 하는 매치 몽을 대기열 재등록
        if (!notExistsMatchMongQueuePlayer.isEmpty()) {
            Set<Long> notExistsMatchMongIds = notExistsMatchMongQueuePlayer.stream()
                    .map(QueuePlayer::getMongId)
                    .collect(Collectors.toSet());

            for (QueuePlayer queuePlayer : notExistsMatchMongQueuePlayer) {
                if (notExistsMatchMongIds.contains(queuePlayer.getMongId())) {
                    // 매치 몽이 존재 하지 않는 경우
                    queuePublishPort.publishCreateQueuePlayerFail(queuePlayer);
                    continue;
                }

                // 매치 몽이 존재 하지만, 매칭에 실패한 경우 (다른 매치 몽이 존재하지 않는 경우)
                Optional<QueuePlayer> queuePlayerOptional = matchPersistencePort.createQueuePlayerPort(CreateQueuePlayerVo.builder()
                        .mongId(queuePlayer.getMongId())
                        .deviceId(queuePlayer.getDeviceId())
                        .accountId(queuePlayer.getAccountId())
                        .build());

                // 대기열 등록에 실패한 경우
                if (queuePlayerOptional.isEmpty()) {
                    // 대기열 등록 실패 비동기 응답
                    queuePublishPort.publishCreateQueuePlayerFail(queuePlayer);
                }
            }
        } else {
            // 대기열 차지 않은 경우
            int botMatchPlayerCount = Math.max(0, command.getMatchPlayerCount() - queuePlayers.size());
            // 남은 자리 봇 매치 플레이어로 채움
            for (int count = 0; count < botMatchPlayerCount; count++) {
                matchPlayers.add(MatchPlayer.generateBotMatchPlayer());
            }

            CreateMatchVo createMatchVo = CreateMatchVo.builder()
                    .round(0)
                    .maxRound(Match.getMaxRound())
                    .matchPlayers(matchPlayers)
                    .build();

            // 매치 등록
            Match match = matchPersistencePort.createMatchPort(createMatchVo)
                    .orElseThrow(InvalidCreateMatchException::new);

            // 매칭 성공 비동기 응답
            matchPublishPort.publishMatch(match);
        }
    }
}
