package com.mongs.play.domain.account.service;

import com.mongs.play.domain.account.entity.AccountLog;
import com.mongs.play.domain.account.repository.AccountLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountLogService {

    private final AccountLogRepository accountLogRepository;

    public AccountLog addAccountLong(AccountLog accountLog) {
        return accountLogRepository.save(accountLog);
    }

    public Optional<AccountLog> getAccountLogByLoginAtToday(Long accountId, String deviceId, LocalDate loginAt) {
        return accountLogRepository.findByAccountIdAndDeviceIdAndLoginAt(accountId, deviceId, loginAt);
    }

    public Optional<AccountLog> getTopAccountLog(Long accountId, String deviceId) {
        return accountLogRepository.findTopByAccountIdAndDeviceIdOrderByLoginAt(accountId, deviceId);
    }
}
