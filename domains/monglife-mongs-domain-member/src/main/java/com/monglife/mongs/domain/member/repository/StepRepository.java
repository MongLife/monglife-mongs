package com.monglife.mongs.domain.member.repository;

import com.monglife.mongs.domain.member.entity.StepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StepRepository extends JpaRepository<StepEntity, Long> {

    Optional<StepEntity> findByDeviceId(String deviceId);
}
