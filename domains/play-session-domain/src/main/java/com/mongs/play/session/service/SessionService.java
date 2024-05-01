package com.mongs.play.session.service;

import com.mongs.play.session.domain.Session;
import com.mongs.play.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public Optional<Session> getSession(String refreshToken) {
        return sessionRepository.findById(refreshToken);
    }

    public Optional<Session> getSession(String deviceId, Long accountId) {
        return sessionRepository.findByDeviceIdAndAccountId(deviceId, accountId);
    }

    public Session addSession(Session session) {
        return sessionRepository.save(session);
    }

    public void removeSession(String refreshToken) {
        sessionRepository.deleteById(refreshToken);
    }

    public void removeSessionIfExists(String deviceId, Long accountId) {
        sessionRepository.findByDeviceIdAndAccountId(deviceId, accountId)
                .ifPresent(session -> sessionRepository.deleteById(session.getRefreshToken()));
    }
}
