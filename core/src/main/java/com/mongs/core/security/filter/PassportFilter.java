package com.mongs.core.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.passport.PassportVO;
import com.mongs.core.security.exception.PassportIntegrityException;
import com.mongs.core.security.exception.SecurityErrorCode;
import com.mongs.core.security.principal.PassportDetail;
import com.mongs.core.util.HmacProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@AllArgsConstructor
public class PassportFilter extends GenericFilterBean {

    private final ObjectMapper objectMapper;
    private final HmacProvider hmacProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String passportJson = request.getHeader("passport");

        if (passportJson != null) {
            PassportVO passportVO = objectMapper.readValue(URLDecoder.decode(passportJson, StandardCharsets.UTF_8), PassportVO.class);

            if (!hmacProvider.verifyHmac(passportVO.data(), passportVO.passportIntegrity())) {
                throw new PassportIntegrityException(SecurityErrorCode.UNAUTHORIZED);
            }

            User passport = new PassportDetail(passportVO, passportJson);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(passport, null, passport.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        chain.doFilter(request, response);
    }
}
