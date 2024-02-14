package com.mongs.management.management.repository;

import com.mongs.management.management.entity.Management;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagementRepository extends JpaRepository<Management, Long> {
}
