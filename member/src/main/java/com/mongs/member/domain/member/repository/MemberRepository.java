package com.mongs.member.domain.member.repository;

import com.mongs.member.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccountIdAndIsDeletedIsFalse(Long accountId);
}
