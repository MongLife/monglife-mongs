package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.domain.member.model.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeReadPort {

    Optional<Notice> getNoticePort(Long noticeId);

    List<Notice> getNoticesPort(Integer page, Integer size);
}
