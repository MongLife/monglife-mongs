package com.monglife.mongs.adapter.out.member.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.member.persistence.entity.MemberEntity;

import java.util.Optional;

public interface MemberDslRepository {

    Optional<MemberEntity> findByAccountIdWithLock(Long accountId);
}
