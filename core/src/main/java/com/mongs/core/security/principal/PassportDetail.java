package com.mongs.core.security.principal;

import com.mongs.core.passport.PassportVO;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Getter
@ToString
public class PassportDetail extends User {
    private final long id;
    private final String deviceId;
    private final String email;
    private final String name;

    public PassportDetail(PassportVO passportVO) {
        super(
                passportVO.data().account().email(),
                UUID.randomUUID().toString(),
                Arrays.stream(passportVO.data().account().role().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );

        this.id = passportVO.data().account().id();
        this.deviceId = passportVO.data().account().deviceId();
        this.email = passportVO.data().account().email();
        this.name = passportVO.data().account().name();
    }
}
