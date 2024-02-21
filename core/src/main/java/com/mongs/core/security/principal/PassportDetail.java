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
    private final String email;
    private final String name;

    public PassportDetail(PassportVO passportVO) {
        super(
                passportVO.data().member().email(),
                UUID.randomUUID().toString(),
                Arrays.stream(passportVO.data().member().role().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
        log.info(passportVO.passportIntegrity().toString());

        this.id = passportVO.data().member().id();
        this.email = passportVO.data().member().email();
        this.name = passportVO.data().member().name();
    }
}