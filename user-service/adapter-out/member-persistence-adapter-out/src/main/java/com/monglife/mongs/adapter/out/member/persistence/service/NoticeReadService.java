package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.core.vo.page.PageResult;
import com.monglife.mongs.adapter.out.member.persistence.entity.NoticeEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.NoticeRepository;
import com.monglife.mongs.application.member.port.out.NoticeReadPort;
import com.monglife.mongs.domain.member.model.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeReadService implements NoticeReadPort {

    private final NoticeRepository noticeRepository;

    /**
     * 공지 사항 조회
     * @param noticeId 공지 사항 ID
     * @return 공지 사항 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Notice> getNoticePort(Long noticeId) {
        return noticeRepository.findByNoticeId(noticeId)
                .map(NoticeEntity::toDomain)
                .or(Optional::empty);
    }

    /**
     * 공지 사항 목록 조회
     * @param page 페이지
     * @param size 사이즈
     * @return 공지 사항 도메인 객체 목록
     */
    @Override
    @Transactional
    public PageResult<Notice> getNoticesPort(Integer page, Integer size, Boolean containHided) {

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<NoticeEntity> noticesPage = containHided
                ? noticeRepository.findAll(pageRequest)
                : noticeRepository.findByIsHidedFalse(pageRequest);

        List<Notice> notices = noticesPage.stream()
                .map(NoticeEntity::toDomain)
                .collect(Collectors.toList());

        return PageResult.<Notice>builder()
                .page(page)
                .size(size)
                .totalPage(noticesPage.getTotalPages())
                .isLastPage(page == noticesPage.getTotalPages())
                .result(notices)
                .build();
    }
}
