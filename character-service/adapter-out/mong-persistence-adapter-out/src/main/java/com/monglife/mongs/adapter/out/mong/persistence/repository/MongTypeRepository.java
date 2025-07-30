package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongTypeEntity;
import com.monglife.mongs.adapter.out.mong.persistence.repository.dsl.MongTypeDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MongTypeRepository extends JpaRepository<MongTypeEntity, String>, MongTypeDslRepository {

    Optional<MongTypeEntity> findByComnCode(String mongCode);

    List<MongTypeEntity> findByLevel(Integer level);
}
