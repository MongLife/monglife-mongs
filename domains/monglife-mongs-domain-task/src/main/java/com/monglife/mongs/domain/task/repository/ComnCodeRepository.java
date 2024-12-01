package com.monglife.mongs.domain.task.repository;

import com.monglife.mongs.domain.task.entity.ComnCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComnCodeRepository extends JpaRepository<ComnCodeEntity, String> {

    List<ComnCodeEntity> findByGroupCode(String groupCode);
}
