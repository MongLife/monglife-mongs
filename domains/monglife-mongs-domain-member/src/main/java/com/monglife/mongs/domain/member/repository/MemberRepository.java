package com.monglife.mongs.domain.member.repository;

import com.monglife.mongs.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByAccountId(Long accountId);
}
