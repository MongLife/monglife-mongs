package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.domain.member.model.Player;

import java.util.Optional;

public interface MemberReadPort {

    Boolean isExistsPlayerPort(Long accountId);

    Optional<Player> getPlayerPort(Long accountId);
}
