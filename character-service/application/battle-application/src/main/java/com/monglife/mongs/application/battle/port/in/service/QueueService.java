package com.monglife.mongs.application.battle.port.in.service;

import com.monglife.mongs.application.battle.port.exception.InvalidCreateMatchException;
import com.monglife.mongs.application.battle.port.exception.InvalidCreateQueuePlayerException;
import com.monglife.mongs.application.battle.port.exception.NotExistsQueuePlayerException;
import com.monglife.mongs.application.battle.port.in.QueueUseCase;
import com.monglife.mongs.application.battle.port.in.command.CreateQueuePlayerCommand;
import com.monglife.mongs.application.battle.port.in.command.DeleteQueuePlayerCommand;
import com.monglife.mongs.application.battle.port.in.command.MatchingQueuePlayersCommand;
import com.monglife.mongs.application.battle.port.out.MatchPersistencePort;
import com.monglife.mongs.application.battle.port.out.MongPersistencePort;
import com.monglife.mongs.application.battle.port.out.QueuePublishPort;
import com.monglife.mongs.application.battle.port.out.vo.CreateMatchVo;
import com.monglife.mongs.application.battle.port.out.vo.CreateQueuePlayerVo;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import com.monglife.mongs.domain.battle.model.QueuePlayer;
import com.monglife.mongs.domain.battle.vo.GenerateMatchPlayerVo;
import com.monglife.mongs.domain.mong.model.Mong;
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

    private final MongPersistencePort mongPersistencePort;

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

        queuePlayer = matchPersistencePort.deleteQueuePlayerPort(queuePlayer)
                .orElseThrow(NotExistsQueuePlayerException::new);

        return queuePlayer;
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

        // 몽 정보가 없는 매치 대기열 도메인 목록
        List<QueuePlayer> notExistsMongQueuePlayer = new ArrayList<>();
        // 매칭이 완료된 매치 플레이어 목록
        List<MatchPlayer> matchPlayers = new ArrayList<>();

        queuePlayers.forEach(queuePlayer -> {
            // 몽 조회
            Optional<Mong> mongOptional = mongPersistencePort.getMongPort(queuePlayer.getMongId());

            // 몽이 존재 하지 않는 경우
            if (mongOptional.isEmpty()) {
                // 몽이 존재 하지 않는 매치 대기열 저장
                notExistsMongQueuePlayer.add(queuePlayer);
            }

            // 몽이 존재 하는 경우
            else {
                Mong mong = mongOptional.get();
                // 몽 배팅 페이 포인트 차감
                mong.matchBetting(Match.getBettingPayPoint());
                // 몽을 매치 플레이어 변환 후 저장
                GenerateMatchPlayerVo generateMatchPlayerVo = GenerateMatchPlayerVo.builder()
                        .accountId(mong.getAccountId())
                        .mongId(mong.getMongId())
                        .mongTypeCode(mong.getMongTypeCode())
                        .mongTypeName(mong.getMongTypeName())
                        .mongName(mong.getMongName())
                        .strength(mong.getStrength())
                        .fatigue(mong.getFatigue())
                        .weight(mong.getWeight())
                        .build();

                matchPlayers.add(MatchPlayer.generateMatchPlayer(queuePlayer, generateMatchPlayerVo));
            }
        });

        // 존재 하지 않는 몽이 있는 경우, 존재 하는 몽을 대기열 재등록
        if (!notExistsMongQueuePlayer.isEmpty()) {
            Set<Long> notExistsMongIds = notExistsMongQueuePlayer.stream()
                    .map(QueuePlayer::getMongId)
                    .collect(Collectors.toSet());

            for (QueuePlayer queuePlayer : notExistsMongQueuePlayer) {
                if (notExistsMongIds.contains(queuePlayer.getMongId())) {
                    // 몽이 존재 하지 않는 경우
                    queuePublishPort.publishMatchingQueuePlayerFailPort(queuePlayer);
                    continue;
                }

                // 몽이 존재 하지만, 매칭에 실패한 경우 (다른 몽이 존재 하지 않는 경우)
                Optional<QueuePlayer> queuePlayerOptional = matchPersistencePort.createQueuePlayerPort(CreateQueuePlayerVo.builder()
                        .mongId(queuePlayer.getMongId())
                        .deviceId(queuePlayer.getDeviceId())
                        .accountId(queuePlayer.getAccountId())
                        .build());

                // 대기열 등록에 실패한 경우
                if (queuePlayerOptional.isEmpty()) {
                    // 대기열 등록 실패 비동기 응답
                    queuePublishPort.publishMatchingQueuePlayerFailPort(queuePlayer);
                }
            }
        } else {
            // 대기열 차지 않은 경우
            int botMatchPlayerCount = Math.max(0, command.getMatchPlayerCount() - queuePlayers.size());
            // 봇 매치 플레이어 생성 후 등록
            matchPlayers.addAll(MatchPlayer.generateBotMatchPlayers(botMatchPlayerCount));

            CreateMatchVo createMatchVo = CreateMatchVo.builder()
                    .matchPlayers(matchPlayers)
                    .build();

            // 매치 등록
            Match match = matchPersistencePort.createMatchPort(createMatchVo)
                    .orElseThrow(InvalidCreateMatchException::new);

            // 매칭 성공 비동기 응답
            queuePublishPort.publishMatchingQueuePlayerPort(match);
        }
    }
}
