package com.monglife.mongs.application.member.port.out;

public interface MemberEventPort {

    void exchangeStarPointEventPort(Long mongId, Integer starPoint, Integer payPoint);
}
