package com.monglife.mongs.application.member.port.out;

import com.monglife.core.vo.page.PageResult;
import com.monglife.mongs.domain.member.model.Notice;

import java.util.Optional;

public interface NoticeReadPort {

    Optional<Notice> getNoticePort(Long noticeId);

    PageResult<Notice> getNoticesPort(Integer page, Integer size, Boolean containHided);
}
