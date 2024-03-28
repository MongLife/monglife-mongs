package com.mongs.member.domain.collection.security;

import com.mongs.core.vo.passport.PassportData;
import com.mongs.core.vo.passport.PassportAccount;
import com.mongs.core.vo.passport.PassportVo;
import com.mongs.core.security.principal.PassportDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.UUID;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockPassportDetail> {
    @Override
    public SecurityContext createSecurityContext(WithMockPassportDetail passportDetail) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        PassportVo passportVO = PassportVo.builder()
                .data(PassportData.builder()
                        .account(PassportAccount.builder()
                                .id(1L)
                                .deviceId("testDeviceId")
                                .email("test@test.com")
                                .name("testName")
                                .role("NORMAL")
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
