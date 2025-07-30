package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.port.out.vo.CreatePlayerVo;
import com.monglife.mongs.domain.member.model.Player;

import java.util.Optional;

public interface MemberPersistencePort {

    Optional<Player> createPlayerPort(CreatePlayerVo createPlayerVo);

    Optional<Player> getPlayerPort(Long accountId);

    Optional<Player> savePlayerPort(Player player);
}
