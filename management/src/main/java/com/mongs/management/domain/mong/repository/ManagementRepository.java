package com.mongs.management.domain.mong.repository;

import com.mongs.management.domain.mong.entity.Management;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagementRepository extends JpaRepository<Management, Long> {
    Optional<Management> findManagementByMemberId (Long memberId);
}
