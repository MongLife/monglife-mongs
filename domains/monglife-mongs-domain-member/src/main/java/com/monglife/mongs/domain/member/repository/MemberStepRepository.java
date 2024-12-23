package com.monglife.mongs.domain.member.repository;

import com.monglife.mongs.domain.member.entity.MemberStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberStepRepository extends JpaRepository<MemberStepEntity, Long> {

    Optional<MemberStepEntity> findByDeviceId(String deviceId);

    Optional<MemberStepEntity> findByMemberAccountIdAndDeviceId(Long accountId, String deviceId);
}
