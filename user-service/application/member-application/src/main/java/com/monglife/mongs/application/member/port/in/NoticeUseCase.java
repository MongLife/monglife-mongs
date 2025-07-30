package com.monglife.mongs.application.member.port.in;

import com.monglife.core.vo.page.PageResult;
import com.monglife.mongs.application.member.port.in.command.GetNoticeCommand;
import com.monglife.mongs.application.member.port.in.command.GetNoticesCommand;
import com.monglife.mongs.domain.member.model.Notice;

public interface NoticeUseCase {

    /**
     * 공지 사항 조회
     */
    Notice getNoticeUseCase(GetNoticeCommand command);

    /**
     * 공지 사항 목록 조회
     */
    PageResult<Notice> getNoticesUseCase(GetNoticesCommand command);
}
