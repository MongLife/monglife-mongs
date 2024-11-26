package com.monglife.mongs.module.jpa.repository;

import com.monglife.mongs.module.jpa.domain.ComnCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComnCodeRepository extends JpaRepository<ComnCodeEntity, String> {

    List<ComnCodeEntity> findByGroupCode(String groupCode);
}
