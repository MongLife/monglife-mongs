package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.mongs.adapter.out.member.persistence.entity.MemberEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.MemberRepository;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.vo.CreatePlayerVo;
import com.monglife.mongs.domain.member.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberPersistenceService implements MemberPersistencePort {

    private final MemberRepository memberRepository;

    /**
     * 플레이어 등록
     * @return 플레이어 도메인 옵셔널 객체
     */
    @Override
    @Transactional
    public Optional<Player> createPlayerPort(CreatePlayerVo createPlayerVo) {

        Optional<MemberEntity> memberEntityOptional = memberRepository.findByAccountId(createPlayerVo.getAccountId());

        if (memberEntityOptional.isEmpty()) {
            MemberEntity memberEntity = MemberEntity.builder()
                    .accountId(createPlayerVo.getAccountId())
                    .slotCount(createPlayerVo.getSlotCount())
                    .starPoint(createPlayerVo.getStarPoint())
                    .build();

            return Optional.of(memberRepository.save(memberEntity).toDomain());
        } else {
            return Optional.empty();
        }
    }

    /**
     * 플레이어 존재 여부 조회
     * @param accountId 회원 ID
     * @return 플레이어 존재 여부
     */
    @Override
    @Transactional
    public Boolean isExistsPlayerPort(Long accountId) {
        return memberRepository.existsByAccountId(accountId);
    }

    /**
     * 플레이어 조회
     * @param accountId 회원 ID
     * @return 플레이어 도메인 옵셔널 객체
     */
    @Override
    @Transactional
    public Optional<Player> getPlayerPort(Long accountId) {
        return memberRepository.findByAccountId(accountId).map(MemberEntity::toDomain);
    }

    /**
     * 플레이어 수정
     * @param player 플레이어 도메인 객체
     * @return 플레이어 도메인 옵셔널 객체
     */
    @Override
    @Transactional
    public Optional<Player> savePlayerPort(Player player) {

        Optional<MemberEntity> memberEntityOptional = memberRepository.findByAccountId(player.getAccountId());

        if (memberEntityOptional.isPresent()) {
            memberEntityOptional.get().update(player);
            return Optional.of(memberEntityOptional.get().toDomain());
        } else {
            return Optional.empty();
        }
    }
}
