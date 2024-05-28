package com.mongs.play.domain.account.service;

import com.mongs.play.core.error.domain.AccountErrorCode;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.account.entity.Account;
import com.mongs.play.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(transactionManager = "accountTransactionManager")
    public Account getAccountByEmailAddIfNotExist(String email, String name) {
        return accountRepository.findByEmailAndIsDeletedFalse(email)
                .orElseGet(() -> accountRepository.save(Account.builder()
                        .name(name)
                        .email(email)
                        .build()));
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public Account getAccountById(Long accountId) throws NotFoundException {
        return accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new NotFoundException(AccountErrorCode.NOT_FOUND_ACCOUNT));
    }
}
