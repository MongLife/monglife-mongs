package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.domain.model.Player;

public interface MemberEventPort {

    void exchangeStarPointEventPort(Player player);
}
