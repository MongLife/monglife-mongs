package com.mongs.auth.repository;

import com.mongs.auth.client.FindMemberResDto;
import com.mongs.auth.client.MemberClient;
import com.mongs.auth.client.RegisterMemberResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MemberClient memberClient;

    public Optional<FindMemberResDto> findMember(Long accountId) {
        ResponseEntity<FindMemberResDto> response = memberClient.findMember(accountId);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }

        return Optional.empty();
    }

    public Optional<RegisterMemberResDto> registerMember(Long accountId) {
        ResponseEntity<RegisterMemberResDto> response = memberClient.registerMember(accountId);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }

        return Optional.empty();
    }
}
