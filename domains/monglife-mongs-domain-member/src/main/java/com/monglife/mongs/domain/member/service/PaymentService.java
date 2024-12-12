package com.monglife.mongs.domain.member.service;

import com.monglife.mongs.domain.member.entity.MemberEntity;
import com.monglife.mongs.domain.member.entity.PaymentEntity;
import com.monglife.mongs.domain.member.exception.NotExistsMemberException;
import com.monglife.mongs.domain.member.exception.NotExistsPaymentCodeException;
import com.monglife.mongs.domain.member.repository.ComnCodeRepository;
import com.monglife.mongs.domain.member.repository.MemberRepository;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ComnCodeRepository comnCodeRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void createPayment(Long accountId, String paymentCode, Integer price, String receipt) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(paymentCode)
                .orElseThrow(() -> new NotExistsPaymentCodeException(paymentCode));

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        PaymentEntity paymentEntity = PaymentEntity.builder()
                .accountId(accountId)
                .comn(comnCodeEntity)
                .price(price)
                .receipt(receipt)
                .build();

        memberEntity.joinPayment(paymentEntity);
    }
}
