package com.monglife.mongs.domain.member.repository;

import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComnCodeRepository extends JpaRepository<ComnCodeEntity, String> {

    List<ComnCodeEntity> findByGroupCode(String groupCode);
}
