package com.mongs.play.domain.member.repository;

import com.mongs.play.domain.member.entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Member> findByAccountIdAndIsDeletedIsFalse(Long accountId);
}
