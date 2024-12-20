package com.monglife.mongs.domain.member.service;

import com.monglife.mongs.domain.member.entity.MemberEntity;
import com.monglife.mongs.domain.member.entity.ProductOrderEntity;
import com.monglife.mongs.domain.member.exception.NotExistsPaymentCodeException;
import com.monglife.mongs.domain.member.exception.NotExistsProductOrderException;
import com.monglife.mongs.domain.member.repository.ComnCodeRepository;
import com.monglife.mongs.domain.member.repository.MemberRepository;
import com.monglife.mongs.domain.member.repository.ProductOrderRepository;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductOrderService {

    private final ComnCodeRepository comnCodeRepository;

    private final MemberRepository memberRepository;
    private final ProductOrderRepository productOrderRepository;

    @Transactional
    public Long createProductOrder(Long accountId, String productId, Integer price) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(productId)
                .orElseThrow(() -> new NotExistsPaymentCodeException(productId));

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseGet(() -> memberRepository.save(new MemberEntity(accountId)));

        ProductOrderEntity productOrderEntity = ProductOrderEntity.builder()
                .accountId(accountId)
                .comn(comnCodeEntity)
                .price(price)
                .build();

        memberEntity.joinProductOrder(productOrderEntity);

        return productOrderEntity.getProductOrderId();
    }

    @Transactional
    public void consumeProductOrder(Long productOrderId, String receipt) {

        ProductOrderEntity productOrderEntity = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new NotExistsProductOrderException(productOrderId));

        productOrderEntity.consume(receipt);
    }

    @Transactional
    public void doneProductOrder(Long productOrderId) {

    }
}
