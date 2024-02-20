package com.mongs.core.security.passport;

import com.mongs.core.passport.PassportVO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@ToString
public class Passport extends User {
    private final long id;
    private final String email;
    private final String name;

    public Passport(PassportVO passportVO) {
        super(
                passportVO.data().member().email(),
                UUID.randomUUID().toString(),
                Arrays.stream(passportVO.data().member().role().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );

        this.id = passportVO.data().member().id();
        this.email = passportVO.data().member().email();
        this.name = passportVO.data().member().name();
    }
}
