package com.mongs.play.domain.account.service;

import com.mongs.play.core.error.domain.AccountErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.account.entity.AccountLog;
import com.mongs.play.domain.account.repository.AccountLogRepository;
import com.mongs.play.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountLogService {

    private final AccountRepository accountRepository;
    private final AccountLogRepository accountLogRepository;

    @Transactional(transactionManager = "accountTransactionManager")
    public AccountLog modifyLoginCountAddAccountLogIfNotExist(Long accountId, String deviceId) throws NotFoundException {

        accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new NotFoundException(AccountErrorCode.NOT_FOUND_ACCOUNT));

        LocalDate today = LocalDate.now();

        AccountLog accountLog = accountLogRepository.findByAccountIdAndDeviceIdAndLoginAtWithLock(accountId, deviceId, today)
                .orElseGet(() -> accountLogRepository.save(AccountLog.builder()
                                .accountId(accountId)
                                .deviceId(deviceId)
                                .loginAt(today)
                        .build()));

        return accountLogRepository.save(accountLog.toBuilder()
                .loginCount(accountLog.getLoginCount() + 1)
                .build());
    }
}
