package com.mongs.auth.repository;

import com.mongs.auth.client.MemberClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MemberClient memberClient;

    public void registerMember(Long accountId) throws RuntimeException {
        ResponseEntity<Object> response = memberClient.registerMember(accountId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            // TODO("예외처리 부분 수정")
            throw new RuntimeException();
        }
    }
}
