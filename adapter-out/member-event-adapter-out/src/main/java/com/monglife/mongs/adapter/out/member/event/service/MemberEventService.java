package com.monglife.mongs.adapter.out.member.event.service;

import com.monglife.mongs.application.member.domain.Player;
import com.monglife.mongs.application.member.port.out.MemberEventPort;
import org.springframework.stereotype.Service;

@Service
public class MemberEventService implements MemberEventPort {

    @Override
    public void exchangeStarPointPort(Player player) {

    }
}
