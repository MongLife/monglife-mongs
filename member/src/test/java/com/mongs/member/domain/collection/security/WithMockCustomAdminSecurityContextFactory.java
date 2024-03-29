package com.mongs.member.domain.collection.security;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.core.vo.passport.PassportAccount;
import com.mongs.core.vo.passport.PassportData;
import com.mongs.core.vo.passport.PassportVO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.UUID;

public class WithMockCustomAdminSecurityContextFactory implements WithSecurityContextFactory<WithMockPassportDetailAdmin> {
    @Override
    public SecurityContext createSecurityContext(WithMockPassportDetailAdmin passportDetail) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        PassportVO passportVO = PassportVO.builder()
                .data(PassportData.builder()
                        .account(PassportAccount.builder()
                                .id(1L)
                                .deviceId("-")
                                .email("-")
                                .name("-")
                                .role("ADMIN")
                                .build())
                        .build())
                .passportIntegrity("passportIntegrity")
                .build();

        PassportDetail principal = new PassportDetail(passportVO);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, UUID.randomUUID().toString(), principal.getAuthorities());

        context.setAuthentication(authentication);

        return context;
    }
}
