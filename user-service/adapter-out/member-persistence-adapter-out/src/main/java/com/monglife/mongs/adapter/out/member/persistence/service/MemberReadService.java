package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.mongs.adapter.out.member.persistence.entity.MemberEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.MemberRepository;
import com.monglife.mongs.application.member.port.out.MemberReadPort;
import com.monglife.mongs.domain.member.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberReadService implements MemberReadPort {

    private final MemberRepository memberRepository;

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
}
