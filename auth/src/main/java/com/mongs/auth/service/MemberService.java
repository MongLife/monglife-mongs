package com.mongs.auth.service;

import com.mongs.auth.client.MemberClient;
import com.mongs.auth.client.dto.response.RegisterMemberResDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberService {
    private final MemberClient memberClient;

    public Optional<RegisterMemberResDto> registerMember(Long accountId) {
        try {
            ResponseEntity<RegisterMemberResDto> response = memberClient.registerMember(accountId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("[{}] registerMember 통신 실패 : {}", accountId, e.getMessage());
        }

        return Optional.empty();
    }
}
