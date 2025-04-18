package com.monglife.mongs.adapter.out.member.persistence.repository;

import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMapEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.dsl.CollectionMapDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionMapRepository extends JpaRepository<CollectionMapEntity, Long>, CollectionMapDslRepository {

    Boolean existsByAccountIdAndMapTypeCode(Long accountId, String mapTypeCode);
}
