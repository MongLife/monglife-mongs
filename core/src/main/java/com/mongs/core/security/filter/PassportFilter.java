package com.mongs.core.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.vo.passport.PassportVo;
import com.mongs.core.security.exception.PassportIntegrityException;
import com.mongs.core.security.exception.SecurityErrorCode;
import com.mongs.core.security.principal.PassportDetail;
import com.mongs.core.utils.HmacProvider;
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

    /**
     * Header 에 담긴 Passport Json 문자열을 파싱하여 인증 객체를 생성하고, SecurityContext 에 저장한다.
     *
     * @param servletRequest  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                     to for further processing
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String passportJson = request.getHeader("passport");

        if (passportJson != null) {
            PassportVo passportVO = objectMapper.readValue(URLDecoder.decode(passportJson, StandardCharsets.UTF_8), PassportVo.class);

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
