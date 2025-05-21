package com.monglife.mongs.adapter.out.member.persistence.repository;

import com.monglife.mongs.adapter.out.member.persistence.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByAccountId(Long accountId);

    Boolean existsByAccountId(Long accountId);
}
