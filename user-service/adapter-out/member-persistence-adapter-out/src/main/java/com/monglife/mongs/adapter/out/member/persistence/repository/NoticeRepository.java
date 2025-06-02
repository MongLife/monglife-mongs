package com.monglife.mongs.adapter.out.member.persistence.repository;

import com.monglife.mongs.adapter.out.member.persistence.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    Optional<NoticeEntity> findByNoticeId(Long noticeId);
}
