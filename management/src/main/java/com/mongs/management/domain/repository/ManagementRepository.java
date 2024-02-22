package com.mongs.management.domain.repository;

import com.mongs.management.domain.entity.Management;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagementRepository extends JpaRepository<Management, Long> {
    Optional<Management> findManagementByMemberId (Long memberId);
}
