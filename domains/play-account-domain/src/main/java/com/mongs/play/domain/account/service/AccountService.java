package com.mongs.play.domain.account.service;

import com.mongs.play.core.error.domain.AccountErrorCode;
import com.mongs.play.core.exception.domain.NotFoundException;
import com.mongs.play.domain.account.entity.Account;
import com.mongs.play.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getAccountByEmailAddIfNotExist(String email, String name) {
        return accountRepository.findByEmailAndIsDeletedFalse(email)
                .orElseGet(() -> accountRepository.save(Account.builder()
                        .name(name)
                        .email(email)
                        .build()));
    }

    public Account getAccountById(Long accountId) throws NotFoundException {
        return accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new NotFoundException(AccountErrorCode.NOT_FOUND_ACCOUNT));
    }
}
