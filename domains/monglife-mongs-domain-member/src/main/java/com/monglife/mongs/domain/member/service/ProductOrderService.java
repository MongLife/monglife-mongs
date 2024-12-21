package com.monglife.mongs.domain.member.service;

import com.monglife.mongs.domain.member.dto.etc.GetProductOrderDto;
import com.monglife.mongs.domain.member.entity.MemberEntity;
import com.monglife.mongs.domain.member.entity.ProductOrderEntity;
import com.monglife.mongs.domain.member.exception.NotExistsMemberException;
import com.monglife.mongs.domain.member.exception.NotExistsProductCodeException;
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

    @Transactional(readOnly = true)
    public GetProductOrderDto getProductOrder(Long productOrderId) {

        ProductOrderEntity productOrderEntity = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new NotExistsProductOrderException(productOrderId));

        return GetProductOrderDto.builder()
                .productId(productOrderEntity.getComn().getCode())
                .build();
    }

    @Transactional
    public Long createProductOrder(Long accountId, String productId, Double price) {

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(productId)
                .orElseThrow(() -> new NotExistsProductCodeException(productId));

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        ProductOrderEntity productOrderEntity = ProductOrderEntity.builder()
                .member(memberEntity)
                .comn(comnCodeEntity)
                .price(price)
                .build();

        productOrderRepository.save(productOrderEntity);

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
