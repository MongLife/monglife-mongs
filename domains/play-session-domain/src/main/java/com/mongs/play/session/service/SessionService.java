package com.mongs.play.session.service;

import com.mongs.play.core.error.domain.SessionErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.session.entity.Session;
import com.mongs.play.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public Session getSession(String refreshToken) throws NotFoundException {
        return sessionRepository.findById(refreshToken)
                .orElseThrow(() -> new NotFoundException(SessionErrorCode.NOT_FOUND_SESSION));
    }

    public Session getSession(String deviceId, Long accountId) throws NotFoundException {
        return sessionRepository.findByDeviceIdAndAccountId(deviceId, accountId)
                .orElseThrow(() -> new NotFoundException(SessionErrorCode.NOT_FOUND_SESSION));
    }

    public Session addSession(String refreshToken, String deviceId, Long accountId, Long expiration) {
        return sessionRepository.save(Session.builder()
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .accountId(accountId)
                .createdAt(LocalDateTime.now())
                .expiration(expiration)
                .build());
    }

    public void removeSession(String refreshToken) throws NotFoundException {

        sessionRepository.findById(refreshToken)
                .orElseThrow(() -> new NotFoundException(SessionErrorCode.NOT_FOUND_SESSION));

        sessionRepository.deleteById(refreshToken);
    }

    public void removeSessionIfExists(String deviceId, Long accountId) throws NotFoundException {

        sessionRepository.findByDeviceIdAndAccountId(deviceId, accountId)
                .ifPresent(session -> sessionRepository.deleteById(session.getRefreshToken()));
    }
}
