package com.monglife.mongs.module.security.global.principal;

import com.monglife.core.vo.passport.PassportVo;
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
public class Passport extends User {
    private final long id;
    private final String deviceId;
    private final String email;
    private final String name;
    private final String passportJson;

    public Passport(PassportVo passportVo, String passportJson) {
        super(
                passportVo.data().account().email(),
                UUID.randomUUID().toString(),
                Arrays.stream(passportVo.data().account().role().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );

        this.id = passportVo.data().account().accountId();
        this.deviceId = passportVo.data().account().deviceId();
        this.email = passportVo.data().account().email();
        this.name = passportVo.data().account().name();
        this.passportJson = passportJson;
    }
}
