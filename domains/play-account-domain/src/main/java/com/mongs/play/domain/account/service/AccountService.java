package com.mongs.play.domain.account.service;

import com.mongs.play.domain.account.entity.Account;
import com.mongs.play.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account addAccount(String email, String name) {
        return accountRepository.save(Account.builder()
                .name(name)
                .email(email)
                .build());
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmailAndIsDeletedFalse(email);
    }

    public Optional<Account> getAccountById(Long accountId) {
        return accountRepository.findByIdAndIsDeletedFalse(accountId);
    }
}
