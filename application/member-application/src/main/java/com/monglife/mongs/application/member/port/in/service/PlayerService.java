package com.monglife.mongs.application.member.port.in.service;

import com.monglife.mongs.application.member.port.exception.InvalidCreatePlayerException;
import com.monglife.mongs.application.member.port.exception.NotExistsPlayerException;
import com.monglife.mongs.application.member.port.in.PlayerUseCase;
import com.monglife.mongs.application.member.port.in.command.*;
import com.monglife.mongs.application.member.port.out.MemberEventPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import com.monglife.mongs.application.member.port.out.vo.CreatePlayerVo;
import com.monglife.mongs.domain.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerService implements PlayerUseCase {

    private static final Integer starPointPerPayPoint = 1000;

    private final MemberPersistencePort memberPersistencePort;

    private final MemberPublishPort memberPublishPort;

    private final MemberEventPort memberEventPort;

    /**
     * 플레이어 등록
     */
    @Override
    @Transactional
    public void createPlayerUseCase(CreatePlayerCommand command) {

        // 플레이어 존재 여부 확인
        if (!memberPersistencePort.isExistsPlayerPort(command.getAccountId())) {
            memberPersistencePort.createPlayerPort(CreatePlayerVo.builder()
                    .accountId(command.getAccountId())
                    .slotCount(1)
                    .starPoint(0)
                    .build())
                    .orElseThrow(InvalidCreatePlayerException::new);
        }
    }

    /**
     * 플레이어 조회
     */
    @Override
    @Transactional
    public Player getPlayerUseCase(GetPlayerCommand command) {

        return memberPersistencePort.getPlayerPort(command.getAccountId())
                .orElseThrow(NotExistsPlayerException::new);
    }

    /**
     * 슬롯 구매
     */
    @Override
    @Transactional
    public Player buySlotUseCase(BuySlotCommand command) {

        Player player = memberPersistencePort.getPlayerPort(command.getAccountId())
                .orElseThrow(NotExistsPlayerException::new);

        // 플레이어 슬롯 구매
        player.buySlot();

        // 플레이어 수정
        memberPersistencePort.savePlayerPort(player)
                .orElseThrow(NotExistsPlayerException::new);

        // 스타 포인트 비동기 응답
        memberPublishPort.publishStarPointPort(player);

        // 슬롯 수 비동기 응답
        memberPublishPort.publishSlotCountPort(player);

        return player;
    }

    /**
     * 스타 포인트 환전
     */
    @Override
    @Transactional
    public Player exchangeStarPointUseCase(ExchangeStarPointCommand command) {

        Player player = memberPersistencePort.getPlayerPort(command.getAccountId())
                .orElseThrow(NotExistsPlayerException::new);

        // 스타 포인트 감소
        player.decreaseStarPoint(command.getStarPoint());

        // 플레이어 수정
        memberPersistencePort.savePlayerPort(player)
                .orElseThrow(NotExistsPlayerException::new);

        // 스타 포인트 환전 이벤트 발생
        int payPoint = command.getStarPoint() * starPointPerPayPoint;
        memberEventPort.exchangeStarPointEventPort(command.getMongId(), command.getStarPoint(), payPoint);

        // 스타 포인트 비동기 응답
        memberPublishPort.publishStarPointPort(player);

        return player;
    }

    /**
     * 스타 포인트 증가
     */
    @Override
    @Transactional
    public Player increaseStarPointUseCase(IncreaseStarPointCommand command) {

        Player player = memberPersistencePort.getPlayerPort(command.getAccountId())
                .orElseThrow(NotExistsPlayerException::new);

        // 스타 포인트 증가
        player.increaseStarPoint(command.getStarPoint());

        // 플레이어 수정
        memberPersistencePort.savePlayerPort(player)
                .orElseThrow(NotExistsPlayerException::new);

        // 스타 포인트 비동기 응답
        memberPublishPort.publishStarPointPort(player);

        return player;
    }
}
