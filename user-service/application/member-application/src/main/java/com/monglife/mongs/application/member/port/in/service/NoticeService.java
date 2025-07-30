package com.monglife.mongs.application.member.port.in.service;

import com.monglife.core.vo.page.PageResult;
import com.monglife.mongs.application.member.port.exception.NotExistsNoticeException;
import com.monglife.mongs.application.member.port.in.NoticeUseCase;
import com.monglife.mongs.application.member.port.in.command.GetNoticeCommand;
import com.monglife.mongs.application.member.port.in.command.GetNoticesCommand;
import com.monglife.mongs.application.member.port.out.NoticeReadPort;
import com.monglife.mongs.domain.member.model.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService implements NoticeUseCase {

    private final NoticeReadPort noticeReadPort;

    /**
     * 공지 사항 조회
     */
    @Override
    public Notice getNoticeUseCase(GetNoticeCommand command) {

        Optional<Notice> noticeOptional = noticeReadPort.getNoticePort(command.getNoticeId());

        if (noticeOptional.isEmpty() || noticeOptional.get().getIsHided()) {
            throw new NotExistsNoticeException();
        }

        return noticeOptional.get();
    }

    /**
     * 공지 사항 목록 조회
     */
    @Override
    public PageResult<Notice> getNoticesUseCase(GetNoticesCommand command) {
        return noticeReadPort.getNoticesPort(command.getPage(), command.getSize(), false);
    }
}
