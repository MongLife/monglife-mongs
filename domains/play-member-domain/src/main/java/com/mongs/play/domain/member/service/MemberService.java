package com.mongs.play.domain.member.service;

import com.mongs.play.core.error.domain.MemberErrorCode;
import com.mongs.play.core.exception.domain.NotFoundException;
import com.mongs.play.domain.member.entity.Member;
import com.mongs.play.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    public Member getMember(Long accountId) throws NotFoundException {
        return memberRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    public Member modifyMaxSlot(Long accountId, Integer maxSlot) throws NotFoundException {

        Member member = memberRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        return memberRepository.save(member.toBuilder()
                .maxSlot(maxSlot)
                .build());
    }

    public Member modifyStarPoint(Long accountId, Integer starPoint) throws NotFoundException {

        Member member = memberRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(MemberErrorCode.NOT_FOUND_MEMBER));

        return memberRepository.save(member.toBuilder()
                .starPoint(starPoint)
                .build());
    }
}
