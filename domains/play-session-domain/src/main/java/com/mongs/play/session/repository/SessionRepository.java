package com.mongs.play.session.repository;

import com.mongs.play.session.domain.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {
    Optional<Session>  findByDeviceIdAndAccountId(String deviceId, Long accountId);
}
