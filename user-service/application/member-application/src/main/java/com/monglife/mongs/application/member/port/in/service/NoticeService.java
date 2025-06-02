package com.monglife.mongs.application.member.port.in.service;

import com.monglife.mongs.application.member.port.exception.NotExistsNoticeException;
import com.monglife.mongs.application.member.port.in.NoticeUseCase;
import com.monglife.mongs.application.member.port.in.command.GetNoticeCommand;
import com.monglife.mongs.application.member.port.in.command.GetNoticesCommand;
import com.monglife.mongs.application.member.port.out.NoticePersistencePort;
import com.monglife.mongs.domain.member.model.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService implements NoticeUseCase {

    private final NoticePersistencePort noticePersistencePort;

    /**
     * 공지 사항 조회
     */
    @Override
    public Notice getNoticeUseCase(GetNoticeCommand command) {

        Optional<Notice> noticeOptional = noticePersistencePort.getNoticePort(command.getNoticeId());

        if (noticeOptional.isEmpty() || noticeOptional.get().getIsHided()) {
            throw new NotExistsNoticeException();
        }

        return noticeOptional.get();
    }

    /**
     * 공지 사항 목록 조회
     */
    @Override
    public List<Notice> getNoticesUseCase(GetNoticesCommand command) {
        return noticePersistencePort.getNoticesPort(command.getPage() - 1, command.getSize()).stream()
                .filter(notice -> !notice.getIsHided())
                .collect(Collectors.toList());
    }
}
