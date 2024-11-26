package com.monglife.mongs.module.security.global.principal;

import com.monglife.core.vo.passport.PassportVo;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@ToString
public class Passport extends User {

    private final Long accountId;

    private final String deviceId;

    private final String email;

    private final String name;

    private final LocalDateTime createdAt;

    public Passport(PassportVo passportVo) {
        super(
                passportVo.data().account().email(),
                UUID.randomUUID().toString(),
                Arrays.stream(passportVo.data().account().role().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );

        this.accountId = passportVo.data().account().accountId();
        this.deviceId = passportVo.data().account().deviceId();
        this.email = passportVo.data().account().email();
        this.name = passportVo.data().account().name();
        this.createdAt = passportVo.getCreatedAt();
    }
}
