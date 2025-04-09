package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.domain.Player;

public interface MemberEventPort {

    void exchangeStarPointPort(Player player);
}
